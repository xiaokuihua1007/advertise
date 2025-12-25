package org.classmatechen.sample.job;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.classmatechen.basic.group.Param;
import org.classmatechen.oceanengine.DyContextImpl;
import org.classmatechen.oceanengine.request.AdvertiserInfoGet;
import org.classmatechen.oceanengine.request.AdvertiserListGet;
import org.classmatechen.oceanengine.request.FileImageGetGet;
import org.classmatechen.oceanengine.request.FileVideoGetGet;
import org.classmatechen.oceanengine.request.FundGetGet;
import org.classmatechen.oceanengine.request.ProjectListGet;
import org.classmatechen.oceanengine.request.PromotionListGet;
import org.classmatechen.oceanengine.request.ReportCustomConfigGetGet;
import org.classmatechen.sample.entity.DyAdvertiserID;
import org.classmatechen.sample.mapper.OceanengineMapper;
import org.classmatechen.sample.mongo.MongoStore;
import org.classmatechen.sample.po.Oceanengine;
import org.classmatechen.sample.sample.oceanengine.AdvertiserGetApiSampler;
import org.classmatechen.sample.sample.oceanengine.AdvertiserInfoGetSampler;
import org.classmatechen.sample.sample.oceanengine.AdvertiserListGetSampler;
import org.classmatechen.sample.sample.oceanengine.FileImageGetGetSampler;
import org.classmatechen.sample.sample.oceanengine.FileVideoGetGetSampler;
import org.classmatechen.sample.sample.oceanengine.FundGetGetSampler;
import org.classmatechen.sample.sample.oceanengine.LogSearchGetSampler;
import org.classmatechen.sample.sample.oceanengine.ProjectListGetSampler;
import org.classmatechen.sample.sample.oceanengine.PromotionListGetSampler;
import org.classmatechen.sample.sample.oceanengine.ReportCustomConfigGetGetSampler;
import org.classmatechen.sample.sample.oceanengine.ReportCustomGetGetSampler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bytedance.ads.model.FileImageGetV2Filtering;
import com.bytedance.ads.model.FileVideoGetV2Filtering;
import com.bytedance.ads.model.ProjectListV30Filtering;
import com.bytedance.ads.model.ProjectListV30FilteringStatusFirst;
import com.bytedance.ads.model.PromotionListV30Filtering;
import com.bytedance.ads.model.PromotionListV30FilteringStatusFirst;
import com.bytedance.ads.model.ReportCustomConfigGetV30DataTopics;
import com.google.common.collect.Lists;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

@Component
public class OceanengineXxljob {

    @Autowired
    private OceanengineMapper oceanengineMapper;

    @Autowired
    private AdvertiserGetApiSampler advertiserGetApiSampler;

    /**
     * 每日基础数据拉取
     */
    @XxlJob("oceanengineDay")
    public void oceanengineDay() {

        XxlJobHelper.log("oceanengineDay begin ...");
        advertiserGetApiSampler();
        XxlJobHelper.log("advertiserGetApiSampler end");
        advertiserListGetSampler();
        XxlJobHelper.log("advertiserListGetSampler end");
        advertiserInfoGetSampler();
        XxlJobHelper.log("advertiserInfoGetSampler end");
        fundGetGetSampler();
        XxlJobHelper.log("fundGetGetSampler end");
        fileImageGetGetSamplerYesterday();
        XxlJobHelper.log("fileImageGetGetSamplerYesterday end");
        fileVideoGetGetSamplerYesterday();
        XxlJobHelper.log("fileVideoGetGetSamplerYesterday end");
        logSearchGetSampler();
        XxlJobHelper.log("logSearchGetSampler end");
        projectListGetSamplerModifyYesterday();
        XxlJobHelper.log("projectListGetSamplerModifyYesterday end");
        promotionListGetSamplerModifyYesterday();
        XxlJobHelper.log("promotionListGetSamplerModifyYesterday end");
        XxlJobHelper.log("oceanengineDay end ...");
    }

    /**
     * 拉取所有组织 UpSert
     */
    @XxlJob("advertiserGetApiSampler")
    public void advertiserGetApiSampler() {

        List<Oceanengine> oceanengines = this.oceanengineMapper.list();
        List<Param<String>> params = oceanengines
            .stream()
            .map(oceanengine -> new Param<>(new DyContextImpl(oceanengine.getAppId()), oceanengine.getAccessToken()))
            .collect(Collectors.toList());
        advertiserGetApiSampler.sample(params);
    }

    @Autowired
    private AdvertiserListGetSampler advertiserListGetSampler;

    /**
     * 拉取所有账户列表 UpSert
     */
    @XxlJob("advertiserListGetSampler")
    public void advertiserListGetSampler() {

        List<DyAdvertiserID> advertisers = MongoStore.list(DyAdvertiserID.class, AdvertiserGetApiSampler.collection);
        List<Param<AdvertiserListGet.Param>> params = advertisers
            .stream()
            .map(advertiser -> {
                AdvertiserListGet.Param param = new AdvertiserListGet.Param();
                param.setCcAccountId(advertiser.getAdvertiserId());
                return new Param<>(new DyContextImpl(advertiser.getAppId()), param);
            })
            .collect(Collectors.toList());
        advertiserListGetSampler.sample(params);
    }

    @Autowired
    private AdvertiserInfoGetSampler advertiserInfoGetSampler;

    /**
     * 拉取所有账户信息 UpSert
     */
    @XxlJob("advertiserInfoGetSampler")
    public void advertiserInfoGetSampler() {

        List<DyAdvertiserID> advertisers = getAdvertisers();
        Map<Long, List<DyAdvertiserID>> partions = advertisers.stream().collect(Collectors.groupingBy(DyAdvertiserID::getAppId));
        List<Param<AdvertiserInfoGet.Param>> params = partions.keySet()
            .stream()
            .map(appId -> {
                List<DyAdvertiserID> innerAdvertisers = partions.get(appId);
                List<List<DyAdvertiserID>> innerPartions = Lists.partition(innerAdvertisers, 50);
                return innerPartions.stream().map(partion -> {
                    AdvertiserInfoGet.Param param = new AdvertiserInfoGet.Param();
                    param.setAdvertiserIds(partion.stream().map(DyAdvertiserID::getAdvertiserId).collect(Collectors.toList()));
                    return new Param<>(new DyContextImpl(appId), param);
                }).collect(Collectors.toList());
            })
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        advertiserInfoGetSampler.sample(params);
    }

    @Autowired
    private FundGetGetSampler fundGetGetSampler;

    /**
     * 拉取所有账户的余额 UpSert
     */
    @XxlJob("fundGetGetSampler")
    public void fundGetGetSampler() {

        List<DyAdvertiserID> advertisers = getAdvertisers();
        List<Param<FundGetGet.Param>> params = advertisers
            .stream()
            .map(advertiser -> {
                FundGetGet.Param param = new FundGetGet.Param();
                param.setAdvertiserId(advertiser.getAdvertiserId());
                return new Param<>(new DyContextImpl(advertiser.getAppId()), param);
            })
            .collect(Collectors.toList());
        fundGetGetSampler.sample(params);
    }

    /**
     * 拉取所有图片 UpSert
     */
    @Autowired
    private FileImageGetGetSampler fileImageGetGetSampler;

    @XxlJob("fileImageGetGetSampler")
    public void fileImageGetGetSampler() {

        List<DyAdvertiserID> advertisers = getAdvertisers();
        List<Param<FileImageGetGet.Param>> params = advertisers
            .stream()
            .map(advertiser -> {
                FileImageGetGet.Param param = new FileImageGetGet.Param();
                param.setAdvertiserId(advertiser.getAdvertiserId());
                param.setPageSize(20L);
                return new Param<>(new DyContextImpl(advertiser.getAppId()), param);
            })
            .collect(Collectors.toList());
        fileImageGetGetSampler.sample(params);
    }

    /**
     * 拉取昨天上传的图片 UpSert
     */
    @XxlJob("fileImageGetGetSamplerYesterday")
    public void fileImageGetGetSamplerYesterday() {

        List<DyAdvertiserID> advertisers = getAdvertisers();
        List<Param<FileImageGetGet.Param>> params = advertisers
            .stream()
            .map(advertiser -> {
                FileImageGetGet.Param param = new FileImageGetGet.Param();
                param.setAdvertiserId(advertiser.getAdvertiserId());
                param.setPageSize(20L);
                FileImageGetV2Filtering filtering = new FileImageGetV2Filtering();
                filtering.setStartTime(yesterday());
                filtering.setEndTime(yesterday());
                param.setFiltering(filtering);
                return new Param<>(new DyContextImpl(advertiser.getAppId()), param);
            })
            .collect(Collectors.toList());
        fileImageGetGetSampler.sample(params);
    }

    @Autowired
    private FileVideoGetGetSampler fileVideoGetGetSampler;

    /**
     * 拉取所有视频 UpSert
     */
    @XxlJob("fileVideoGetGetSampler")
    public void fileVideoGetGetSampler() {

        List<DyAdvertiserID> advertisers = getAdvertisers();
        List<Param<FileVideoGetGet.Param>> params = advertisers
            .stream()
            .map(advertiser -> {
                FileVideoGetGet.Param param = new FileVideoGetGet.Param();
                param.setAdvertiserId(advertiser.getAdvertiserId());
                param.setPageSize(20L);
                return new Param<>(new DyContextImpl(advertiser.getAppId()), param);
            })
            .collect(Collectors.toList());
        fileVideoGetGetSampler.sample(params);
    }

    /**
     * 拉取昨天上传的视频 UpSert
     */
    @XxlJob("fileVideoGetGetSamplerYesterday")
    public void fileVideoGetGetSamplerYesterday() {

        List<DyAdvertiserID> advertisers = getAdvertisers();
        List<Param<FileVideoGetGet.Param>> params = advertisers
            .stream()
            .map(advertiser -> {
                FileVideoGetGet.Param param = new FileVideoGetGet.Param();
                param.setAdvertiserId(advertiser.getAdvertiserId());
                param.setPageSize(20L);
                FileVideoGetV2Filtering filtering = new FileVideoGetV2Filtering();
                filtering.setStartTime(yesterday());
                filtering.setEndTime(yesterday());
                param.setFiltering(filtering);
                return new Param<>(new DyContextImpl(advertiser.getAppId()), param);
            })
            .collect(Collectors.toList());
        fileVideoGetGetSampler.sample(params);
    }

    @Autowired
    private LogSearchGetSampler logSearchGetSampler;

    /**
     *  拉取昨天的日志 Delete And Insert
     */
    @XxlJob("logSearchGetSampler")
    public void logSearchGetSampler() {

        List<DyAdvertiserID> advertisers = getAdvertisers();
        List<Param<LogSearchGetSampler.Inner>> params = advertisers
            .stream()
            .map(advertiser -> {
                LogSearchGetSampler.Inner param = new LogSearchGetSampler.Inner();
                param.setAdvertiserId(advertiser.getAdvertiserId());
                return new Param<>(new DyContextImpl(advertiser.getAppId()), param);
            })
            .collect(Collectors.toList());;
        logSearchGetSampler.sample(params);
    }

    @Autowired
    private ProjectListGetSampler projectListGetSampler;

    /**
     * 拉取所有项目 UpSert
     */
    @XxlJob("projectListGetSampler")
    public void projectListGetSampler() {

        List<DyAdvertiserID> advertisers = getAdvertisers();
        List<Param<ProjectListGet.Param>> params = advertisers
            .stream()
            .map(advertiser -> {
                ProjectListGet.Param param = new ProjectListGet.Param();
                param.setPageSize(10L);
                param.setAdvertiserId(advertiser.getAdvertiserId());
                return new Param<>(new DyContextImpl(advertiser.getAppId()), param);
            })
            .collect(Collectors.toList());
        params.addAll(
            advertisers
                .stream()
                .map(advertiser -> {
                    ProjectListGet.Param param = new ProjectListGet.Param();
                    param.setPageSize(10L);
                    param.setAdvertiserId(advertiser.getAdvertiserId());
                    ProjectListV30Filtering filtering = new ProjectListV30Filtering();
                    filtering.setStatusFirst(ProjectListV30FilteringStatusFirst.DELETE);
                    param.setFiltering(filtering);
                    return new Param<>(new DyContextImpl(advertiser.getAppId()), param);
                })
                .collect(Collectors.toList())
        );
        projectListGetSampler.sample(params);
    }

    /**
     * 拉取昨天创建的项目 UpSert
     */
    @XxlJob("projectListGetSamplerCreateYesterday")
    public void projectListGetSamplerCreateYesterday() {

        List<DyAdvertiserID> advertisers = getAdvertisers();
        List<Param<ProjectListGet.Param>> params = advertisers
            .stream()
            .map(advertiser -> {
                ProjectListGet.Param param = new ProjectListGet.Param();
                param.setPageSize(10L);
                param.setAdvertiserId(advertiser.getAdvertiserId());
                ProjectListV30Filtering filtering = new ProjectListV30Filtering();
                filtering.setProjectCreateTime(yesterday());
                param.setFiltering(filtering);
                return new Param<>(new DyContextImpl(advertiser.getAppId()), param);
            })
            .collect(Collectors.toList());
        params.addAll(
            advertisers
                .stream()
                .map(advertiser -> {
                    ProjectListGet.Param param = new ProjectListGet.Param();
                    param.setPageSize(10L);
                    param.setAdvertiserId(advertiser.getAdvertiserId());
                    ProjectListV30Filtering filtering = new ProjectListV30Filtering();
                    filtering.setProjectCreateTime(yesterday());
                    filtering.setStatusFirst(ProjectListV30FilteringStatusFirst.DELETE);
                    param.setFiltering(filtering);
                    return new Param<>(new DyContextImpl(advertiser.getAppId()), param);
                })
                .collect(Collectors.toList())
        );
        projectListGetSampler.sample(params);
    }

    /**
     * 拉取昨天更新的项目 UpSert
     */
    @XxlJob("projectListGetSamplerModifyYesterday")
    public void projectListGetSamplerModifyYesterday() {

        List<DyAdvertiserID> advertisers = getAdvertisers();
        List<Param<ProjectListGet.Param>> params = advertisers
            .stream()
            .map(advertiser -> {
                ProjectListGet.Param param = new ProjectListGet.Param();
                param.setPageSize(10L);
                param.setAdvertiserId(advertiser.getAdvertiserId());
                ProjectListV30Filtering filtering = new ProjectListV30Filtering();
                filtering.setProjectModifyTime(yesterday());
                param.setFiltering(filtering);
                return new Param<>(new DyContextImpl(advertiser.getAppId()), param);
            })
            .collect(Collectors.toList());
        params = 
            advertisers
                .stream()
                .map(advertiser -> {
                    ProjectListGet.Param param = new ProjectListGet.Param();
                    param.setPageSize(10L);
                    param.setAdvertiserId(advertiser.getAdvertiserId());
                    ProjectListV30Filtering filtering = new ProjectListV30Filtering();
                    filtering.setProjectModifyTime(yesterday());
                    filtering.setStatusFirst(ProjectListV30FilteringStatusFirst.DELETE);
                    param.setFiltering(filtering);
                    return new Param<>(new DyContextImpl(advertiser.getAppId()), param);
                })
                .collect(Collectors.toList())
        ;
        projectListGetSampler.sample(params);
    }

    @Autowired
    private PromotionListGetSampler promotionListGetSampler;

    /**
     * 拉取所有广告 UpSert
     */
    @XxlJob("promotionListGetSampler")
    public void promotionListGetSampler() {

        List<DyAdvertiserID> advertisers = getAdvertisers();
        List<Param<PromotionListGet.Param>> params = advertisers
            .stream()
            .map(advertiser -> {
                PromotionListGet.Param param = new PromotionListGet.Param();
                param.setPageSize(10L);
                param.setAdvertiserId(advertiser.getAdvertiserId());
                PromotionListV30Filtering filtering = new PromotionListV30Filtering();
                filtering.setStatusFirst(PromotionListV30FilteringStatusFirst.ALL);
                param.setFiltering(filtering);
                return new Param<>(new DyContextImpl(advertiser.getAppId()), param);
            })
            .collect(Collectors.toList());
        promotionListGetSampler.sample(params);
    }

    /**
     * 拉取昨天创建的广告 UpSert
     */
    @XxlJob("promotionListGetSamplerCreateYesterday")
    public void promotionListGetSamplerCreateYesterday() {

        List<DyAdvertiserID> advertisers = getAdvertisers();
        List<Param<PromotionListGet.Param>> params = advertisers
            .stream()
            .map(advertiser -> {
                PromotionListGet.Param param = new PromotionListGet.Param();
                param.setPageSize(10L);
                param.setAdvertiserId(advertiser.getAdvertiserId());
                PromotionListV30Filtering filtering = new PromotionListV30Filtering();
                filtering.setPromotionCreateTime(yesterday());
                filtering.setStatusFirst(PromotionListV30FilteringStatusFirst.ALL);
                param.setFiltering(filtering);
                return new Param<>(new DyContextImpl(advertiser.getAppId()), param);
            })
            .collect(Collectors.toList());
        promotionListGetSampler.sample(params);
    }

    /**
     * 拉取昨天更新的广告 UpSert
     */
    @XxlJob("promotionListGetSamplerModifyYesterday")
    public void promotionListGetSamplerModifyYesterday() {

        List<DyAdvertiserID> advertisers = getAdvertisers();
        List<Param<PromotionListGet.Param>> params = advertisers
            .stream()
            .map(advertiser -> {
                PromotionListGet.Param param = new PromotionListGet.Param();
                param.setPageSize(10L);
                param.setAdvertiserId(advertiser.getAdvertiserId());
                PromotionListV30Filtering filtering = new PromotionListV30Filtering();
                filtering.setPromotionModifyTime(yesterday());
                filtering.setStatusFirst(PromotionListV30FilteringStatusFirst.ALL);
                param.setFiltering(filtering);
                return new Param<>(new DyContextImpl(advertiser.getAppId()), param);
            })
            .collect(Collectors.toList());
        promotionListGetSampler.sample(params);
    }

    @Autowired
    private ReportCustomConfigGetGetSampler reportCustomConfigGetGetSampler;

    /**
     * 查询指标配置 Delete And Insert
     */
    @XxlJob("reportCustomConfigGetGetSampler")
    public void reportCustomConfigGetGetSampler() {

        DyAdvertiserID advertiser = getAdvertisers().get(0);
        Param<ReportCustomConfigGetGet.Param> param = new Param<>(
            new DyContextImpl(advertiser.getAppId()),
            new ReportCustomConfigGetGet.Param(
                advertiser.getAdvertiserId(),
                Arrays.asList(
                    ReportCustomConfigGetV30DataTopics.BASIC_DATA,
                    ReportCustomConfigGetV30DataTopics.BIDWORD_DATA,
                    ReportCustomConfigGetV30DataTopics.CREATIVE_DATA,
                    ReportCustomConfigGetV30DataTopics.DPA_VIDEO_DATA,
                    ReportCustomConfigGetV30DataTopics.DMP_DATA,
                    ReportCustomConfigGetV30DataTopics.MATERIAL_DATA,
                    ReportCustomConfigGetV30DataTopics.ONE_KEY_BOOST_DATA,
                    ReportCustomConfigGetV30DataTopics.PRODUCT_DATA,
                    ReportCustomConfigGetV30DataTopics.QUERY_DATA,
                    ReportCustomConfigGetV30DataTopics.VIDEO_DUARATION_DATA
                )
            )
        );
        reportCustomConfigGetGetSampler.sample(param);
    }

    @Autowired
    private ReportCustomGetGetSampler reportCustomGetGetSampler;

    /**
     * 拉取昨天创意指标 Upsert
     */
    @XxlJob("reportCustomGetGetSampler")
    public void reportCustomGetGetSampler() {

        List<Param<ReportCustomGetGetSampler.SamplerParam>> params = getAdvertisers()
            .stream()
            .map(advertiser -> {
                ReportCustomGetGetSampler.SamplerParam param = new ReportCustomGetGetSampler.SamplerParam();
                param.setAdvertiserId(advertiser.getAdvertiserId());
                param.setStartTime(yesterdayStartTime());
                param.setEndTime(yesterdayEndTime());
                return new Param<>(new DyContextImpl(advertiser.getAppId()), param);
            })
            .collect(Collectors.toList());
        reportCustomGetGetSampler.sample(params);
    }
    
    private List<DyAdvertiserID> getAdvertisers() {

        return MongoStore.list(DyAdvertiserID.class, AdvertiserListGetSampler.collection);
    }

    private String yesterday() {

        return new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
    }

    private String yesterdayStartTime() {

        return yesterday() + " 00:00:00";
    }

    private String yesterdayEndTime() {

        return yesterday() + " 23:59:59";
    }
}
