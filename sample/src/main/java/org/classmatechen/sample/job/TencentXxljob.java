package org.classmatechen.sample.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.classmatechen.basic.group.Param;
import org.classmatechen.sample.entity.TxAdvertiserID;
import org.classmatechen.sample.mongo.MongoStore;
import org.classmatechen.sample.sample.tencent.AdGroupsGetSampler;
import org.classmatechen.sample.sample.tencent.AdvertiserGetSampler;
import org.classmatechen.sample.sample.tencent.DynamicCreativesGetSampler;
import org.classmatechen.sample.sample.tencent.HourlyReportsGetSampler;
import org.classmatechen.sample.sample.tencent.OrganizationAccountRelationGetSampler;
import org.classmatechen.sample.sample.tencent.TargetingTagReportsGetSampler;
import org.classmatechen.tencent.TxContextImpl;
import org.classmatechen.tencent.request.AdGroupsGet;
import org.classmatechen.tencent.request.AdvertiserGet;
import org.classmatechen.tencent.request.DynamicCreativesGet;
import org.classmatechen.tencent.request.HourlyReportsGet;
import org.classmatechen.tencent.request.TargetingTagReportsGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tencent.ads.model.v3.HourlyReportDateRange;
import com.tencent.ads.model.v3.ReportDateRange;
import com.xxl.job.core.handler.annotation.XxlJob;

@Component
public class TencentXxljob {

    @Autowired
    private OrganizationAccountRelationGetSampler organizationAccountRelationGetSampler;

    @XxlJob("organizationAccountRelationGetSampler")
    public void organizationAccountRelationGetSampler() {

        organizationAccountRelationGetSampler.sample(new ArrayList<>());
    }

    @Autowired
    private AdvertiserGetSampler advertiserGetSampler;

    @XxlJob("advertiserGetSampler")
    public void advertiserGetSampler() {

        List<TxAdvertiserID> advertisers = MongoStore.list(TxAdvertiserID.class, OrganizationAccountRelationGetSampler.collection);
        List<Param<AdvertiserGet.Param>> params = advertisers
            .stream()
            .map(advertiser -> {
                AdvertiserGet.Param param = new AdvertiserGet.Param();
                param.setAccountId(advertiser.getAccountId());
                param.setPageSize(50L);
                param.setFields(Arrays.asList(
                    "account_id",
                    "daily_budget",
                    "registration_type",
                    "corporation_name",
                    "corporation_licence",
                    "certification_image_id",
                    "certification_image",
                    "individual_qualification",
                    "area_code",
                    "mdm_id",
                    "mdm_name",
                    "system_industry_id",
                    "customized_industry",
                    "introduction_url",
                    "corporate_brand_name",
                    "memo",
                    "system_status",
                    "reject_message",
                    "is_adx",
                    "business_alias",
                    "contact_person",
                    "contact_person_email",
                    "contact_person_telephone",
                    "contact_person_mobile",
                    "websites",
                    "agency_account_id",
                    "operators"
                ));
                return new Param<>(new TxContextImpl(advertiser.getClientId()), param);
            })
            .collect(Collectors.toList());
        advertiserGetSampler.sample(params);
    }

    @Autowired
    private AdGroupsGetSampler adGroupsGetSampler;

    @XxlJob("adGroupsGetSampler")
    public void adGroupsGetSampler() {

        List<TxAdvertiserID> advertisers = MongoStore.list(TxAdvertiserID.class, OrganizationAccountRelationGetSampler.collection);
        List<Param<AdGroupsGet.Param>> params = advertisers
            .stream()
            .map(advertiser -> {
                AdGroupsGet.Param param = new AdGroupsGet.Param();
                param.setAccountId(advertiser.getAccountId());
                param.setPageSize(50L);
                param.setFields(Arrays.asList(
                    "targeting",
                    "adgroup_id",
                    "targeting_translation",
                    "configured_status",
                    "created_time",
                    "last_modified_time",
                    "is_deleted",
                    "system_status",
                    "adgroup_name",
                    "marketing_goal",
                    "marketing_sub_goal",
                    "marketing_carrier_type",
                    "marketing_carrier_detail",
                    "marketing_target_type",
                    "marketing_target_detail",
                    "marketing_target_id",
                    "begin_date",
                    "end_date",
                    "first_day_begin_time",
                    "bid_amount",
                    "optimization_goal",
                    "time_series",
                    "automatic_site_enabled",
                    "site_set",
                    "daily_budget",
                    "scene_spec",
                    "user_action_sets",
                    "deep_conversion_spec",
                    "conversion_id",
                    "deep_conversion_behavior_bid",
                    "deep_conversion_worth_rate",
                    "deep_conversion_worth_advanced_rate",
                    "deep_conversion_behavior_advanced_bid",
                    "bid_mode",
                    "auto_acquisition_enabled",
                    "auto_acquisition_budget",
                    "smart_bid_type",
                    "smart_cost_cap",
                    "auto_derived_creative_enabled",
                    "search_expand_targeting_switch",
                    "auto_derived_landing_page_switch",
                    "data_model_version",
                    "bid_scene",
                    "marketing_target_ext",
                    "deep_optimization_type",
                    "flow_optimization_enabled",
                    "marketing_target_attachment",
                    "negative_word_cnt",
                    "search_expansion_switch",
                    "marketing_asset_id",
                    "promoted_asset_type",
                    "material_package_id",
                    "marketing_asset_outer_spec",
                    "poi_list",
                    "marketing_scene",
                    "exploration_strategy",
                    "priority_site_set",
                    "ecom_pkam_switch",
                    "forward_link_assist",
                    "conversion_name",
                    "auto_acquisition_status",
                    "cost_constraint_scene",
                    "custom_cost_cap",
                    "mpa_spec",
                    "short_play_pay_type",
                    "sell_strategy_id",
                    "og_completion_type",
                    "dca_spec",
                    "aoi_optimization_strategy",
                    "cost_guarantee_status",
                    "cost_guarantee_money",
                    "additional_product_spec",
                    "enable_breakthrough_siteset",
                    "live_recommend_strategy_enabled",
                    "custom_cost_roi_cap",
                    "enable_steady_exploration",
                    "adx_realtime_type",
                    "smart_targeting_status"
                ));
                return new Param<>(new TxContextImpl(advertiser.getClientId()), param);
            })
            .collect(Collectors.toList());
        adGroupsGetSampler.sample(params);
    }

    @Autowired
    private DynamicCreativesGetSampler dynamicCreativesGetSampler;

    @XxlJob("dynamicCreativesGetSampler")
    public void dynamicCreativesGetSampler() {

        List<TxAdvertiserID> advertisers = MongoStore.list(TxAdvertiserID.class, OrganizationAccountRelationGetSampler.collection);
        List<Param<DynamicCreativesGet.Param>> params = advertisers
            .stream()
            .map(advertiser -> {
                DynamicCreativesGet.Param param = new DynamicCreativesGet.Param();
                param.setAccountId(advertiser.getAccountId());
                param.setPageSize(50L);
                param.setFields(Arrays.asList(
                    "adgroup_id",
                    "dynamic_creative_id",
                    "dynamic_creative_name",
                    "creative_template_id",
                    "delivery_mode",
                    "dynamic_creative_type",
                    "creative_components",
                    "impression_tracking_url",
                    "click_tracking_url",
                    "program_creative_info",
                    "page_track_url",
                    "configured_status",
                    "is_deleted",
                    "created_time",
                    "last_modified_time",
                    "marketing_asset_verification",
                    "creative_set_approval_status",
                    "source",
                    "asset_inconsistent_status"
                ));
                return new Param<>(new TxContextImpl(advertiser.getClientId()), param);
            })
            .collect(Collectors.toList());
        dynamicCreativesGetSampler.sample(params);
    }

    @Autowired
    private HourlyReportsGetSampler hourlyReportsGetSampler;

    @XxlJob("hourlyReportsGetSampler")
    public void hourlyReportsGetSampler() {

        final String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24));

        List<TxAdvertiserID> advertisers = MongoStore.list(TxAdvertiserID.class, OrganizationAccountRelationGetSampler.collection);
        List<Param<HourlyReportsGet.Param>> params = advertisers
            .stream()
            .map(advertiser -> {
                HourlyReportsGet.Param param = new HourlyReportsGet.Param();
                param.setAccountId(advertiser.getAccountId());
                param.setLevel("REPORT_LEVEL_DYNAMIC_CREATIVE");
                HourlyReportDateRange range = new HourlyReportDateRange();
                range.setStartDate(yesterday);
                range.setEndDate(yesterday);
                param.setDateRange(range);
                param.setPageSize(50L);
                param.setFields(Arrays.asList(
                    "hour",
                    "account_id",
                    "adgroup_id",
                    "dynamic_creative_id",
                    "view_count", // 曝光次数。广告被展示给用户的次数
                    "valid_click_count", // 点击次数。广告被用户点击的次数
                    "cost", // 花费。广告主为广告投放总共付出的费用成本，实际花费请以财务记录为准
                    "conversions_count", // 目标转化量。优化目标的转化量
                    "deep_conversions_count", // 深度目标转化量。深度优化目标的转化量（如果没有使用深度转化优化功能，这个指标可能为空）
                    "ctr", // 点击率。广告被点击的比率，计算公式是：点击次数/曝光次数*100%
                    "conversions_rate", // 目标转化率。优化目标的点击转化率，计算公式是：目标转化量/点击次数*100%
                    "conversions_cost", // 目标转化成本。广告主为每个目标转化量付出的费用成本，计算公式是：花费/目标转化量
                    "thousand_display_price", // 千次展现均价。广告平均每一千次展现所付出的费用，计算公式是：花费/曝光次数*1000
                    "deep_conversions_cost", // 深度转化成本。广告主为每个深度目标转化量付出的费用成本，计算公式是：花费/深度目标转化量
                    "cpc" // 点击均价。广告主为每次点击付出的费用成本，计算公式是：花费/点击次数
                ));
                param.setGroupBy(Arrays.asList("hour", "account_id", "adgroup_id", "dynamic_creative_id"));
                return new Param<>(new TxContextImpl(advertiser.getClientId()), param);
            })
            .collect(Collectors.toList());
        hourlyReportsGetSampler.sample(params);
    }

    @Autowired
    private TargetingTagReportsGetSampler targetingTagReportsGetSampler;

    @XxlJob("targetingTagReportsGetSampler")
    public void targetingTagReportsGetSampler() {

        final String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24));

        List<TxAdvertiserID> advertisers = MongoStore.list(TxAdvertiserID.class, OrganizationAccountRelationGetSampler.collection);
        List<Param<TargetingTagReportsGet.Param>> params = advertisers
            .stream()
            .map(advertiser -> {
                TargetingTagReportsGet.Param param = new TargetingTagReportsGet.Param();
                param.setAccountId(advertiser.getAccountId());
                param.setType("CITY");
                param.setLevel("ADGROUP");
                ReportDateRange range = new ReportDateRange();
                range.setStartDate(yesterday);
                range.setEndDate(yesterday);
                param.setDateRange(range);
                param.setFiltering(Arrays.asList());
                param.setGroupBy(Arrays.asList("date", "adgroup_id", "city_id"));
                param.setPageSize(50L);
                param.setFields(Arrays.asList(
                    "adgroup_id",
                    "date",
                    "city_id",
                    "view_count", // 曝光次数。广告被展示给用户的次数
                    "valid_click_count", // 点击次数。广告被用户点击的次数
                    "cost", // 花费。广告主为广告投放总共付出的费用成本，实际花费请以财务记录为准
                    "conversions_count", // 目标转化量。优化目标的转化量
                    "deep_conversions_count", // 深度目标转化量。深度优化目标的转化量（如果没有使用深度转化优化功能，这个指标可能为空）
                    "ctr", // 点击率。广告被点击的比率，计算公式是：点击次数/曝光次数*100%
                    "conversions_rate", // 目标转化率。优化目标的点击转化率，计算公式是：目标转化量/点击次数*100%
                    "conversions_cost", // 目标转化成本。广告主为每个目标转化量付出的费用成本，计算公式是：花费/目标转化量
                    "thousand_display_price", // 千次展现均价。广告平均每一千次展现所付出的费用，计算公式是：花费/曝光次数*1000
                    "deep_conversions_cost", // 深度转化成本。广告主为每个深度目标转化量付出的费用成本，计算公式是：花费/深度目标转化量
                    "cpc" // 点击均价。广告主为每次点击付出的费用成本，计算公式是：花费/点击次数
                ));
                return new Param<>(new TxContextImpl(advertiser.getClientId()), param);
            })
            .collect(Collectors.toList());
        targetingTagReportsGetSampler.sample(params);
    }
}
