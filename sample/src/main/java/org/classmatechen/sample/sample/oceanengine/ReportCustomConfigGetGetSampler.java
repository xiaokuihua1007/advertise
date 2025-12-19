package org.classmatechen.sample.sample.oceanengine;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bson.Document;
import org.classmatechen.basic.group.Consumer;
import org.classmatechen.basic.group.Param;
import org.classmatechen.basic.group.impl.ListGroup;
import org.classmatechen.oceanengine.request.ReportCustomConfigGetGet;
import org.classmatechen.sample.mongo.MongoRowBuilder;
import org.classmatechen.sample.mongo.MongoStore;
import org.classmatechen.sample.sample.Sampler;
import org.classmatechen.sample.sample.SamplerUtil;
import org.springframework.stereotype.Service;

import com.bytedance.ads.model.ReportCustomConfigGetV30DataListDataTopic;
import com.bytedance.ads.model.ReportCustomConfigGetV30ResponseDataListInner;
import com.mongodb.client.model.UpdateOneModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReportCustomConfigGetGetSampler implements Sampler<ReportCustomConfigGetGet.Param> {

    public static final String collection = "Dy_ReportCustomConfigGetGet";

    @Override
    public void sample(List<Param<ReportCustomConfigGetGet.Param>> params) {
        
        Consumer<ReportCustomConfigGetGet.Param, List<ReportCustomConfigGetV30ResponseDataListInner>> consumer = (context, _param, list) -> {
            List<UpdateOneModel<Document>> documents = list
                    .stream()
                    .filter(data -> {
                        if (Objects.nonNull(data.getDataTopic())) {
                            return true;
                        } else {
                            log.warn("ReportCustomConfigGetGetSampler null data: {}", data);
                            return false;
                        }
                    })
                    .map(data -> {
                        ReportCustomConfigGetV30DataListDataTopic topic = data.getDataTopic();
                        return new MongoRowBuilder<>(data)
                                                .id(topic.getValue())
                                                .build();
                    })
                    .collect(Collectors.toList());
                MongoStore.store(collection, documents);
        };

        new ListGroup<>(SamplerUtil.create(ReportCustomConfigGetGet.class), params.get(0), consumer).execute();
    }
}
