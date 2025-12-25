package org.classmatechen.sample.sample.oceanengine;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.classmatechen.basic.group.Consumer;
import org.classmatechen.basic.group.Param;
import org.classmatechen.basic.group.impl.PageGroup;
import org.classmatechen.oceanengine.DyContext;
import org.classmatechen.oceanengine.request.PromotionListGet;
import org.classmatechen.sample.mongo.MongoRowBuilder;
import org.classmatechen.sample.mongo.MongoStore;
import org.classmatechen.sample.sample.Sampler;
import org.classmatechen.sample.sample.SamplerUtil;
import org.springframework.stereotype.Service;

import com.bytedance.ads.model.PromotionListV30ResponseDataListInner;
import com.mongodb.client.model.UpdateOneModel;

@Service
public class PromotionListGetSampler implements Sampler<PromotionListGet.Param> {

    public static final String collection = "Dy_PromotionListGet";

    @Override
    public void sample(List<Param<PromotionListGet.Param>> params) {
        
        Consumer<PromotionListGet.Param, List<PromotionListV30ResponseDataListInner>> consumer = (context, param, list) -> {
            List<UpdateOneModel<Document>> documents = list
                    .stream()
                    .map(data -> new MongoRowBuilder<>(data)
                                                .removeNull()
                                                .id(data.getPromotionId())
                                                .append("appId", ((DyContext) context).getAppId())
                                                .build()
                    )
                    .collect(Collectors.toList());
                MongoStore.store(collection, documents);
        };

        new PageGroup<>(SamplerUtil.page(PromotionListGet.class), params, consumer).execute();
    }
}
