package org.classmatechen.sample.sample.oceanengine;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.classmatechen.basic.group.Consumer;
import org.classmatechen.basic.group.Param;
import org.classmatechen.basic.group.impl.PageGroup;
import org.classmatechen.oceanengine.DyContext;
import org.classmatechen.oceanengine.request.ProjectListGet;
import org.classmatechen.sample.mongo.MongoRowBuilder;
import org.classmatechen.sample.mongo.MongoStore;
import org.classmatechen.sample.sample.Sampler;
import org.classmatechen.sample.sample.SamplerUtil;
import org.springframework.stereotype.Service;

import com.bytedance.ads.model.ProjectListV30ResponseDataListInner;
import com.mongodb.client.model.UpdateOneModel;

@Service
public class ProjectListGetSampler implements Sampler<ProjectListGet.Param> {

    public static final String collection = "Dy_ProjectListGet";

    @Override
    public void sample(List<Param<ProjectListGet.Param>> params) {
        
        Consumer<ProjectListGet.Param, List<ProjectListV30ResponseDataListInner>> consumer = (context, param, list) -> {
            List<UpdateOneModel<Document>> documents = list
                    .stream()
                    .map(data -> new MongoRowBuilder<>(data)
                                                // .removeNull()
                                                // .removeEmpty()
                                                // .removeBlank()
                                                .id(data.getProjectId())
                                                .append("appId", ((DyContext) context).getAppId())
                                                .build()
                    )
                    .collect(Collectors.toList());
                MongoStore.store(collection, documents);
        };

        new PageGroup<>(SamplerUtil.page(ProjectListGet.class), params, consumer).execute();
    }
}
