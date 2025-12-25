package org.classmatechen.sample.sample.oceanengine;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.classmatechen.basic.group.Consumer;
import org.classmatechen.basic.group.Param;
import org.classmatechen.basic.group.impl.PageGroup;
import org.classmatechen.oceanengine.request.ReportCustomGetGet;
import org.classmatechen.sample.po.AdvertiseMetrics;
import org.classmatechen.sample.sample.Sampler;
import org.classmatechen.sample.sample.SamplerUtil;
import org.classmatechen.sample.service.AdvertiseMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bytedance.ads.model.ReportCustomGetV30DataTopic;
import com.bytedance.ads.model.ReportCustomGetV30ResponseDataRowsInner;
import lombok.Data;

/**
 * 统计当天每个小时每个广告的指标
 */
@Service
public class ReportCustomGetGetSampler implements Sampler<ReportCustomGetGetSampler.SamplerParam> {

    @Autowired
    private AdvertiseMetricsService advertiseMetricsService;

    @Override
    public void sample(List<Param<ReportCustomGetGetSampler.SamplerParam>> sParams) {

        List<Param<ReportCustomGetGet.Param>> params = sParams.stream().map(param -> param(param)).collect(Collectors.toList());
        
        Consumer<ReportCustomGetGet.Param, List<ReportCustomGetV30ResponseDataRowsInner>> consumer = (context, param, list) -> {

            List<AdvertiseMetrics> metrics = list
                .stream()
                .map(data -> {
                        String promotion_id = data.getDimensions().get("cdp_promotion_id");
                        String time = data.getDimensions().get("stat_time_hour");
                        Date date = null;
                        try {
                            date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(time.substring(0, 16));
                        } catch (ParseException e) {

                        }
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        // String shortTime = Long.toString((calendar.getTimeInMillis() / 1000));

                        AdvertiseMetrics metric = new AdvertiseMetrics();
                        metric.setAdvertiseId(Long.parseLong(promotion_id));
                        // metric.setTime(Long.parseLong(shortTime));
                        metric.setHumanTime(calendar.getTime());
                        // metric.setYear(calendar.get(Calendar.YEAR));
                        // metric.setMonth(calendar.get(Calendar.MONTH) + 1);
                        // metric.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                        // metric.setHour(calendar.get(Calendar.HOUR_OF_DAY));
                        metric.setStatCost(new BigDecimal(data.getMetrics().get("stat_cost")));
                        metric.setShowCnt(Long.parseLong(data.getMetrics().get("show_cnt")));
                        metric.setCpmPlatform(new BigDecimal(data.getMetrics().get("cpm_platform")));
                        metric.setClickCnt(Long.parseLong(data.getMetrics().get("click_cnt")));
                        metric.setCpcPlatform(new BigDecimal(data.getMetrics().get("cpc_platform")));
                        metric.setCtr(new BigDecimal(data.getMetrics().get("ctr")));
                        metric.setAttributionConvertCnt(Long.parseLong(data.getMetrics().get("attribution_convert_cnt")));
                        metric.setAttributionConvertCost(new BigDecimal(data.getMetrics().get("attribution_convert_cost")));
                        metric.setConvertCnt(Long.parseLong(data.getMetrics().get("convert_cnt")));
                        metric.setConversionCost(new BigDecimal(data.getMetrics().get("conversion_cost")));
                        metric.setConversionRate(new BigDecimal(data.getMetrics().get("conversion_rate")));
                        metric.setActive(Long.parseLong(data.getMetrics().get("active")));
                        metric.setActiveCost(new BigDecimal(data.getMetrics().get("active_cost")));
                        metric.setActiveRate(new BigDecimal(data.getMetrics().get("active_rate")));
                        return metric; 
                    }
                )
                .collect(Collectors.toList());
            advertiseMetricsService.insert(metrics);
        };

        new PageGroup<>(SamplerUtil.page(ReportCustomGetGet.class), params, consumer).execute();
    }

    private Param<ReportCustomGetGet.Param> param(Param<SamplerParam> sParam) {

        SamplerParam samplerParam = sParam.getParam();
        ReportCustomGetGet.Param param = new ReportCustomGetGet.Param();
        param.setAdvertiserId(samplerParam.getAdvertiserId());
        param.setDataTopic(ReportCustomGetV30DataTopic.BASIC_DATA);
        param.setDimensions(Arrays.asList(
            "stat_time_hour",
            "cdp_promotion_id"
        ));
        param.setMetrics(Arrays.asList(
            "stat_cost", // 消耗(元),表示广告在投放期内的预估花费金额。当天数据可能会有波动，次日稳定
            "show_cnt", // 展示数,广告展示给用户的次数。计算方式：经平台判定有效且被计费的展示次数。
            "cpm_platform", // 平均千次展现费用(元),广告平均每一千次展现所付出的费用，计算公式是：总消耗/展示数*1000。
            "click_cnt", // 点击数,当用户点击广告素材时，触发点击事件，该事件被认为是一次有效的广告点击。
            "cpc_platform", // 平均点击单价(元),广告主为每次点击付出的费用成本，计算公式是：总消耗/点击数。
            "ctr", // 点击率,广告被点击的次数占展示次数的百分比。计算方法：点击数/展示数*100%
            "attribution_convert_cnt", // 转化数(计费时间),在转化行为发生（或回传）之后，将转化行为回记到过去30天内的扣费（消耗产生）时间上。 例如：广告在8月20日展示给用户，此时广告花费10元，用户点击广告后于8月23日产生1笔购买，则8月23日这笔购买将会展示在8月20日，8月23日没有转化数。
            "attribution_convert_cost", // 转化成本(计费时间),转化成本(计费时间) = 消耗 / 转化数(计费时间)。例如：广告在8月20日展示给用户，此时广告花费10元，用户点击广告后，于8月23日产生2笔购买，则8月20日的转化成本（计费时间） = 5元（即10元除以2笔）。成本考核和系统赔付以该指标为准。
            "convert_cnt", // 转化数,按转化事件发生时间统计的转化数。建议广告主考核成本时参考“转化数据(计费时间)”，例如您的广告在早上8点进行了展示和点击，用户晚上19点发生了激活行为，我们会把激活数披露在晚上19点。在线索行业中， 表单提交、私信留资、留资咨询、智能电话-确认拨打所对应的事件都会计入广告转化数。
            "conversion_cost", // 平均转化成本,广告主为每个转化所付出的平均成本，计算方式：总消耗/转化数。当天数据可能会有波动。
            "conversion_rate", // 转化率,广告被用户转化的次数占点击次数的百分比。计算方式：转化数/点击数*100%
            "active", // 激活数,如果您对接了API，激活数是您认可且回传成功的的激活数。如果您对接了SDK，则激活数是指用户下载您的APP后打开的次数。
            "active_cost", // 激活成本（元）,计算方式：总花费/激活数。
            "active_rate" // 激活率,计算方式：激活数/点击数*100%
        ));
        param.setFilters(Arrays.asList());
        param.setStartTime(samplerParam.getStartTime());
        param.setEndTime(samplerParam.getEndTime());
        param.setOrderBy(Arrays.asList());
        param.setPageSize(50L);
        return new Param<>(sParam.getContext(), param);
    }

    @Data
    public static class SamplerParam {
    
        private Long advertiserId;
        private String startTime;
        private String endTime;
    }
}
