package org.classmatechen.sample.sample.tencent;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.classmatechen.basic.group.Consumer;
import org.classmatechen.basic.group.Param;
import org.classmatechen.basic.group.impl.PageGroup;
import org.classmatechen.sample.mongo.MongoRowBuilder;
import org.classmatechen.sample.mongo.MongoStore;
import org.classmatechen.sample.sample.Sampler;
import org.classmatechen.sample.sample.SamplerUtil;
import org.classmatechen.tencent.request.TargetingTagReportsGet;
import org.springframework.stereotype.Service;

import com.mongodb.client.model.UpdateOneModel;
import com.tencent.ads.model.v3.TargetReportApiListStruct;

@Service
public class TargetingTagReportsGetSampler implements Sampler<TargetingTagReportsGet.Param> {

    public static final String collection = "Tx_TargetingTagReportsGetSampler";

    @Override
    public void sample(List<Param<TargetingTagReportsGet.Param>> params) {

        Consumer<TargetingTagReportsGet.Param, List<TargetReportApiListStruct>> consumer = (context, param, list) -> {
            List<UpdateOneModel<Document>> documents = list
                    .stream()
                    .map(data -> new MongoRowBuilder<>()
                                                .id(Long.toString(data.getAdgroupId()) + "-" + data.getDate() + "-" + Long.valueOf(data.getCityId()))
                                                .append("adgroup_id", data.getAdgroupId())
                                                .append("date", data.getDate())
                                                .append("city_id", data.getCityId())
                                                .append("view_count", data.getViewCount())
                                                .append("valid_click_count", data.getValidClickCount())
                                                .append("cost", data.getCost())
                                                .append("conversions_count", data.getConversionsCount())
                                                .append("deep_conversions_count", data.getDeepConversionsCount())
                                                .append("ctr", data.getCtr())
                                                .append("conversions_rate", data.getConversionsByClickRate())
                                                .append("conversions_cost", data.getConversionsCost())
                                                .append("thousand_display_price", data.getThousandDisplayPrice())
                                                .append("deep_conversions_cost", data.getDeepConversionsCost())
                                                .append("cpc", data.getCpc())
                                                .build()
                    )
                    .collect(Collectors.toList());
                MongoStore.store(collection, documents);
        };

        new PageGroup<>(SamplerUtil.page(TargetingTagReportsGet.class), params, consumer).execute();
    }
}
