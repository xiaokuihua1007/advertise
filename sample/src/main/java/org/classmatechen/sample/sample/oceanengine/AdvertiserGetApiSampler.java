package org.classmatechen.sample.sample.oceanengine;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.classmatechen.basic.group.Consumer;
import org.classmatechen.basic.group.Param;
import org.classmatechen.basic.group.impl.ListGroup;
import org.classmatechen.oceanengine.DyContext;
import org.classmatechen.oceanengine.request.AdvertiserGetApi;
import org.classmatechen.sample.mongo.MongoRowBuilder;
import org.classmatechen.sample.mongo.MongoStore;
import org.classmatechen.sample.sample.Sampler;
import org.classmatechen.sample.sample.SamplerUtil;
import org.springframework.stereotype.Service;

import com.bytedance.ads.model.Oauth2AdvertiserGetResponseDataListInner;
import com.mongodb.client.model.UpdateOneModel;

@Service
public class AdvertiserGetApiSampler implements Sampler<String> {

    public static final String collection = "Dy_AdvertiserGetApi";

    @Override
    public void sample(List<Param<String>> params) {
        
        Consumer<String, List<Oauth2AdvertiserGetResponseDataListInner>> consumer = (context, param, list) -> {

            List<UpdateOneModel<Document>> documents = list
                .stream()
                .map(data -> new MongoRowBuilder<>(data)
                                            .removeNull()
                                            .id(data.getAdvertiserId())
                                            .append("appId", ((DyContext) context).getAppId())
                                            .build()
                )
                .collect(Collectors.toList());
            MongoStore.store(collection, documents);
        };

        new ListGroup<>(SamplerUtil.create(AdvertiserGetApi.class), params, consumer).execute();
    }
}
