package org.classmatechen.sample.sample.oceanengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.Document;
import org.classmatechen.basic.group.Consumer;
import org.classmatechen.basic.group.GroupFail;
import org.classmatechen.basic.group.Param;
import org.classmatechen.basic.group.impl.PageGroup;
import org.classmatechen.common.Platform;
import org.classmatechen.oceanengine.DyContextImpl;
import org.classmatechen.oceanengine.request.FileVideoGetGet;
import org.classmatechen.sample.entity.DyAdvertiserID;
import org.classmatechen.sample.event.VideoSampledFailEvent;
import org.classmatechen.sample.event.VideoSampledFailListener;
import org.classmatechen.sample.mongo.MongoRowBuilder;
import org.classmatechen.sample.mongo.MongoStore;
import org.classmatechen.sample.po.PlatformVideo;
import org.classmatechen.sample.sample.AbstarctSampler;
import org.classmatechen.sample.sample.SamplerUtil;
import org.springframework.stereotype.Service;

import com.bytedance.ads.model.FileVideoGetV2Filtering;
import com.bytedance.ads.model.FileVideoGetV2ResponseDataListInner;
import com.google.common.collect.Lists;
import com.mongodb.client.model.UpdateOneModel;

@Service
public class FileVideoGetGetSampler extends AbstarctSampler<FileVideoGetGet.Param> implements VideoSampledFailListener {

    public static final String collection = "Dy_FileVideoGetGet";

    @Override
    public List<GroupFail<FileVideoGetGet.Param>> doSample(List<Param<FileVideoGetGet.Param>> params) {
        
        Consumer<FileVideoGetGet.Param, List<FileVideoGetV2ResponseDataListInner>> consumer = (context, param, list) -> {
            List<UpdateOneModel<Document>> documents = list
                    .stream()
                    .map(data -> new MongoRowBuilder<>(data)
                                                .removeNull()
                                                .id(data.getId())
                                                .append("advertiserId", param.getAdvertiserId())
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

        // Publisher.publish(new VideoSampledEvent(Platform.Oceanengine));
    }

    @Override
    public void onVideoSampledFail(VideoSampledFailEvent event) {
        if (Platform.Oceanengine == event.getPlatform()) {
            List<PlatformVideo> list = event.getList();
            List<DyAdvertiserID> advertisers = MongoStore.list(DyAdvertiserID.class, AdvertiserGetApiSampler.collection);
            Map<Long, Long> advertiserID2AppId = advertisers.stream().collect(Collectors.toMap(DyAdvertiserID::getAdvertiserId, DyAdvertiserID::getAppId));
            Map<Long, List<PlatformVideo>> map = list.stream().collect(Collectors.groupingBy(PlatformVideo::getAccountId));
            List<Param<FileVideoGetGet.Param>> params = new ArrayList<>();
            for (Long advertiserId : map.keySet()) {
                List<PlatformVideo> ids = map.get(advertiserId);
                Long appId = advertiserID2AppId.get(advertiserId);
                if (null == appId) {
                    continue;
                }
                List<List<PlatformVideo>> partitions = Lists.partition(ids, 80);
                for (List<PlatformVideo> partition : partitions) {
                    FileVideoGetGet.Param p = new FileVideoGetGet.Param();
                    p.setAdvertiserId(advertiserId);
                    FileVideoGetV2Filtering filter = new FileVideoGetV2Filtering();
                    for (String id : partition.stream().map(PlatformVideo::getPlatformId).collect(Collectors.toList())) {
                        filter.addVideoIdsItem(id);
                    }
                    p.setFiltering(filter);
                    params.add(new Param<>(new DyContextImpl(appId), p));
                }
            }
            this.sample(params);
        }
    }
}
