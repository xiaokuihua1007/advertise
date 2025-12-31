package org.classmatechen.sample.sample.oceanengine;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bson.Document;
import org.classmatechen.basic.group.Consumer;
import org.classmatechen.basic.group.GroupFail;
import org.classmatechen.basic.group.Param;
import org.classmatechen.basic.group.impl.ListGroup;
import org.classmatechen.oceanengine.DyContext;
import org.classmatechen.oceanengine.client.impl.AccessTokenProvider;
import org.classmatechen.oceanengine.request.AdvertiserGetApi;
import org.classmatechen.sample.mongo.MongoRowBuilder;
import org.classmatechen.sample.mongo.MongoStore;
import org.classmatechen.sample.sample.AbstarctSampler;
import org.classmatechen.sample.sample.SamplerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bytedance.ads.model.Oauth2AdvertiserGetResponseDataListInner;
import com.mongodb.client.model.UpdateOneModel;

@Service
public class AdvertiserGetApiSampler extends AbstarctSampler<String> {

    public static final String collection = "Dy_AdvertiserGetApi";

    @Autowired
    private List<AccessTokenProvider> accessTokenProviders;

    @Override
    public List<GroupFail<String>> doSample(List<Param<String>> params) {

        params = params
            .stream()
            .map(param -> {
                DyContext context = (DyContext) param.getContext();
                try {
                    context.getClient();
                } catch (Exception e) {
                    return null;
                }
                String token = null;
                for (AccessTokenProvider provider : accessTokenProviders) {
                    token = provider.accessToken(context);
                    if (null != token) {
                        return new Param<>(param.getContext(), token);
                    }
                }
                return null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
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

        return new ListGroup<>(SamplerUtil.create(AdvertiserGetApi.class), params, consumer).execute();
    }
}
