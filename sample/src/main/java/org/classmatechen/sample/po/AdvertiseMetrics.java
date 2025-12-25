package org.classmatechen.sample.po;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/*
create table `oceanengine_advertise_metrics` (
    `advertise_id` largeint not null comment "广告id",
    `time` bigint not null comment "时间",
    `human_time` datetime not null comment "时间",
    `year` int not null comment "年",
    `month` int not null comment "月",
    `day` int null comment "日",
    `hour` int null comment "小时",
    `stat_cost` decimal(10, 2) null comment "消耗",
    `show_cnt` bigint null comment "展示数",
    `cpm_platform` decimal(10, 2) null comment "平均千次展现费用(元)",
    `click_cnt` bigint null comment "点击数",
    `cpc_platform` decimal(10, 2) null comment "平均点击单价(元)",
    `ctr` decimal(10, 2) null comment "点击率",
    `attribution_convert_cnt` bigint null comment "转化数(计费时间)",
    `attribution_convert_cost` decimal(10, 2) null comment "转化成本(计费时间)",
    `convert_cnt` bigint null comment "转化数",
    `conversion_cost` decimal(10, 2) null comment "平均转化成本",
    `conversion_rate` decimal(10, 2) null comment "转化率",
    `active` bigint null comment "激活数",
    `active_cost` decimal(10, 2) null comment "激活成本（元）",
    `active_rate` decimal(10, 2) null comment "激活率"
) unique key(
    `advertise_id`,
    `time`
)
distributed by hash (
    `advertise_id`
)
buckets 10 properties (
    "replication_num" = "1"
)
*/
@Data
public class AdvertiseMetrics {

    private Long advertiseId;
    // private Long time;
    private Date humanTime;
    // private Integer year;
    // private Integer month;
    // private Integer day;
    // private Integer hour;
    private BigDecimal statCost;
    private Long showCnt;
    private BigDecimal cpmPlatform;
    private Long clickCnt;
    private BigDecimal cpcPlatform;
    private BigDecimal ctr;
    private Long attributionConvertCnt;
    private BigDecimal attributionConvertCost;
    private Long convertCnt;
    private BigDecimal conversionCost;
    private BigDecimal conversionRate;
    private Long active;
    private BigDecimal activeCost;
    private BigDecimal activeRate;
}
