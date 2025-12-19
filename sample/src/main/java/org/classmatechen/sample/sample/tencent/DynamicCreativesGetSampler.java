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
import org.classmatechen.tencent.TxContext;
import org.classmatechen.tencent.request.DynamicCreativesGet;
import org.springframework.stereotype.Service;

import com.mongodb.client.model.UpdateOneModel;
import com.tencent.ads.model.v3.DynamicCreativesGetListStruct;

@Service
public class DynamicCreativesGetSampler implements Sampler<DynamicCreativesGet.Param> {

    public static final String collection = "Tx_DynamicCreativesGet";

    @Override
    public void sample(List<Param<DynamicCreativesGet.Param>> params) {
        
        Consumer<DynamicCreativesGet.Param, List<DynamicCreativesGetListStruct>> consumer = (context, param, list) -> {
            List<UpdateOneModel<Document>> documents = list
                .stream()
                .map(data -> new MongoRowBuilder<>(data)
                                            .removeNull()
                                            .id(data.getDynamicCreativeId())
                                            .append("clientId", ((TxContext) context).getClientId())
                                            .append("accountId", param.getAccountId())
                                            .build()
                )
                .collect(Collectors.toList());
            MongoStore.store(collection, documents);
        };

        new PageGroup<>(SamplerUtil.page(DynamicCreativesGet.class), params, consumer).execute();
    }
}
