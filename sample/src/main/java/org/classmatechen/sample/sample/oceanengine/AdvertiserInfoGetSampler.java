package org.classmatechen.sample.sample.oceanengine;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.classmatechen.basic.group.Consumer;
import org.classmatechen.basic.group.Param;
import org.classmatechen.basic.group.impl.ListGroup;
import org.classmatechen.oceanengine.DyContext;
import org.classmatechen.oceanengine.request.AdvertiserInfoGet;
import org.classmatechen.sample.mongo.MongoRowBuilder;
import org.classmatechen.sample.mongo.MongoStore;
import org.classmatechen.sample.sample.Sampler;
import org.classmatechen.sample.sample.SamplerUtil;
import org.springframework.stereotype.Service;

import com.bytedance.ads.model.AdvertiserInfoV2ResponseData;
import com.mongodb.client.model.UpdateOneModel;

@Service
public class AdvertiserInfoGetSampler implements Sampler<AdvertiserInfoGet.Param> {

    public static final String collection = "Dy_AdvertiserInfoGet";

    @Override
    public void sample(List<Param<AdvertiserInfoGet.Param>> params) {
        
        Consumer<AdvertiserInfoGet.Param, List<AdvertiserInfoV2ResponseData>> consumer = (context, param, list) -> {
            List<UpdateOneModel<Document>> documents = list
                    .stream()
                    .map(data -> new MongoRowBuilder<>(data)
                                                .removeNull()
                                                .removeEmpty()
                                                .removeBlank()
                                                .id(data.getId())
                                                .append("appId", ((DyContext) context).getAppId())
                                                .build()
                    )
                    .collect(Collectors.toList());
                MongoStore.store(collection, documents);
        };

        new ListGroup<>(SamplerUtil.create(AdvertiserInfoGet.class), params, consumer).execute();
    }
}
