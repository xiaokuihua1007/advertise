package org.classmatechen.sample.sample.oceanengine;

import java.util.List;
import org.bson.Document;
import org.classmatechen.basic.group.Consumer;
import org.classmatechen.basic.group.Param;
import org.classmatechen.basic.group.impl.SimpleGroup;
import org.classmatechen.oceanengine.DyContext;
import org.classmatechen.oceanengine.request.FundGetGet;
import org.classmatechen.sample.mongo.MongoRowBuilder;
import org.classmatechen.sample.mongo.MongoStore;
import org.classmatechen.sample.sample.Sampler;
import org.classmatechen.sample.sample.SamplerUtil;
import org.springframework.stereotype.Service;

import com.bytedance.ads.model.AdvertiserFundGetV2ResponseData;
import com.mongodb.client.model.UpdateOneModel;

@Service
public class FundGetGetSampler implements Sampler<FundGetGet.Param> {

    public static final String collection = "Dy_FundGetGet";

    @Override
    public void sample(List<Param<FundGetGet.Param>> params) {
        
        Consumer<FundGetGet.Param, AdvertiserFundGetV2ResponseData> consumer = (context, param, data) -> {
            UpdateOneModel<Document> document = new MongoRowBuilder<>(data)
                                                .removeNull()
                                                .id(data.getAdvertiserId())
                                                .append("appId", ((DyContext) context).getAppId())
                                                .build();
            MongoStore.store(collection, document);
        };

        new SimpleGroup<>(SamplerUtil.create(FundGetGet.class), params, consumer).execute();
    }
}
