package org.classmatechen.sample.sample.tencent;

import java.util.List;
import org.bson.Document;
import org.classmatechen.basic.group.Consumer;
import org.classmatechen.basic.group.Param;
import org.classmatechen.basic.group.impl.PageGroup;
import org.classmatechen.sample.mongo.MongoRowBuilder;
import org.classmatechen.sample.mongo.MongoStore;
import org.classmatechen.sample.sample.Sampler;
import org.classmatechen.sample.sample.SamplerUtil;
import org.classmatechen.tencent.TxContext;
import org.classmatechen.tencent.request.AdvertiserGet;
import org.springframework.stereotype.Service;

import com.mongodb.client.model.UpdateOneModel;
import com.tencent.ads.model.v3.AdvertiserGetListStruct;

@Service
public class AdvertiserGetSampler implements Sampler<AdvertiserGet.Param> {

    public static final String collection = "Tx_AdvertiserGet";

    @Override
    public void sample(List<Param<AdvertiserGet.Param>> params) {
        
        Consumer<AdvertiserGet.Param, List<AdvertiserGetListStruct>> consumer = (context, param, list) -> {
             if (list.size() == 1) {
                AdvertiserGetListStruct struct = list.get(0);
                UpdateOneModel<Document> document = new MongoRowBuilder<>(struct)
                    .removeNull()
                    .id(struct.getAccountId())
                    .append("clientId", ((TxContext) context).getClientId())
                    .build();
                MongoStore.store(collection, document);
            }
        };

        new PageGroup<>(SamplerUtil.page(AdvertiserGet.class), params, consumer).execute();
    }
}
