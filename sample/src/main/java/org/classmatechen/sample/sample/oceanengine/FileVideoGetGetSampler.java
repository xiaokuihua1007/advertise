package org.classmatechen.sample.sample.oceanengine;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.classmatechen.basic.group.Consumer;
import org.classmatechen.basic.group.GroupFail;
import org.classmatechen.basic.group.Param;
import org.classmatechen.basic.group.impl.PageGroup;
import org.classmatechen.basic.pubsub.Publisher;
import org.classmatechen.common.Platform;
import org.classmatechen.oceanengine.request.FileVideoGetGet;
import org.classmatechen.sample.event.VideoSampledEvent;
import org.classmatechen.sample.mongo.MongoRowBuilder;
import org.classmatechen.sample.mongo.MongoStore;
import org.classmatechen.sample.sample.AbstarctSampler;
import org.classmatechen.sample.sample.SamplerUtil;
import org.springframework.stereotype.Service;

import com.bytedance.ads.model.FileVideoGetV2ResponseDataListInner;
import com.mongodb.client.model.UpdateOneModel;

@Service
public class FileVideoGetGetSampler extends AbstarctSampler<FileVideoGetGet.Param> {

    public static final String collection = "Dy_FileVideoGetGet";

    @Override
    public List<GroupFail<FileVideoGetGet.Param>> doSample(List<Param<FileVideoGetGet.Param>> params) {
        
        Consumer<FileVideoGetGet.Param, List<FileVideoGetV2ResponseDataListInner>> consumer = (context, param, list) -> {
            List<UpdateOneModel<Document>> documents = list
                    .stream()
                    .map(data -> new MongoRowBuilder<>(data)
                                                .removeNull()
                                                .id(data.getId())
                                                // .append("appId", ((DyContext) context).getAppId())
                                                .build()
                    )
                    .collect(Collectors.toList());
                MongoStore.store(collection, documents);
        };

        return new PageGroup<>(SamplerUtil.page(FileVideoGetGet.class), params, consumer).execute();
    }

    @Override
    protected void postProcess() {

        Publisher.publish(new VideoSampledEvent(Platform.Oceanengine));
    }
}
