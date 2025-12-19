package org.classmatechen.sample.sample.oceanengine;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.classmatechen.basic.group.Consumer;
import org.classmatechen.basic.group.Param;
import org.classmatechen.basic.group.impl.PageGroup;
import org.classmatechen.oceanengine.request.LogSearchGet;
import org.classmatechen.sample.mongo.MongoRowBuilder;
import org.classmatechen.sample.mongo.MongoStore;
import org.classmatechen.sample.sample.Sampler;
import org.classmatechen.sample.sample.SamplerUtil;
import org.springframework.stereotype.Service;

import com.bytedance.ads.model.ToolsLogSearchV2ResponseDataLogsInner;
import com.mongodb.client.model.UpdateOneModel;

@Service
public class LogSearchGetSampler implements Sampler<LogSearchGet.Param> {

    public static final String collection = "Dy_LogSearchGet";

    @Override
    public void sample(List<Param<LogSearchGet.Param>> params) {
 
        Consumer<LogSearchGet.Param, List<ToolsLogSearchV2ResponseDataLogsInner>> consumer = (context, param, list) -> {
            List<UpdateOneModel<Document>> documents = list
                    .stream()
                    .map(data -> {
                        String id = new StringBuilder()
                            .append(Long.toString(param.getAdvertiserId()))
                            .append("-")
                            .append(data.getCreateTime().replaceAll("-", "").replaceAll(":", "").replaceAll(" ", ""))
                            .append("-")
                            .append(data.getOperator())
                            .append("-")
                            .append(Long.toString(data.getObjectId()))
                            .toString();
                        return new MongoRowBuilder<>(data)
                                                .id(id)
                                                .append("advertiserId", param.getAdvertiserId())
                                                .build();
                    })
                    .collect(Collectors.toList());
                MongoStore.store(collection, documents);
        };

        new PageGroup<>(SamplerUtil.page(LogSearchGet.class), params, consumer).execute();
    }
}
