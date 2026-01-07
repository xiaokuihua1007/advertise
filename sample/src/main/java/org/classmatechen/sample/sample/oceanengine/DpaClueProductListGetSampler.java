package org.classmatechen.sample.sample.oceanengine;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.classmatechen.basic.group.Consumer;
import org.classmatechen.basic.group.GroupFail;
import org.classmatechen.basic.group.Param;
import org.classmatechen.basic.group.impl.PageGroup;
import org.classmatechen.oceanengine.request.DpaClueProductListGet;
import org.classmatechen.sample.mongo.MongoRowBuilder;
import org.classmatechen.sample.mongo.MongoStore;
import org.classmatechen.sample.sample.AbstarctSampler;
import org.classmatechen.sample.sample.SamplerUtil;
import org.springframework.stereotype.Service;

import com.bytedance.ads.model.DpaClueProductListV2ResponseDataProductsInner;
import com.mongodb.client.model.UpdateOneModel;

@Service
public class DpaClueProductListGetSampler extends AbstarctSampler<DpaClueProductListGet.Param> {

    public static final String collection = "Dy_DpaClueProductListGet";

    @Override
    public List<GroupFail<DpaClueProductListGet.Param>> doSample(List<Param<DpaClueProductListGet.Param>> params) {

        Consumer<DpaClueProductListGet.Param, List<DpaClueProductListV2ResponseDataProductsInner>> consumer = (context, param, list) -> {
            List<UpdateOneModel<Document>> documents = list
                .stream()
                .map(data -> new MongoRowBuilder<>(data)
                                            .removeNull()
                                            .id(data.getProductId())
                                            .append("advertiserId", param.getAdvertiserId())
                                            .build()
                )
                .collect(Collectors.toList());
            MongoStore.store(collection, documents);
        };

        return new PageGroup<>(SamplerUtil.page(DpaClueProductListGet.class), params, consumer).execute();
    }
}
