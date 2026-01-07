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
import org.classmatechen.basic.pubsub.Publisher;
import org.classmatechen.common.Platform;
import org.classmatechen.oceanengine.DyContextImpl;
import org.classmatechen.oceanengine.request.FileImageGetGet;
import org.classmatechen.sample.entity.DyAdvertiserID;
import org.classmatechen.sample.event.ImageSampledFailEvent;
import org.classmatechen.sample.event.ImageSampledFailListener;
import org.classmatechen.sample.mongo.MongoRowBuilder;
import org.classmatechen.sample.mongo.MongoStore;
import org.classmatechen.sample.po.PlatformImage;
import org.classmatechen.sample.sample.AbstarctSampler;
import org.classmatechen.sample.sample.SamplerUtil;
import org.springframework.stereotype.Service;

import com.bytedance.ads.model.FileImageGetV2Filtering;
import com.bytedance.ads.model.FileImageGetV2ResponseDataListInner;
import com.google.common.collect.Lists;
import com.mongodb.client.model.UpdateOneModel;

@Service
public class FileImageGetGetSampler extends AbstarctSampler<FileImageGetGet.Param> implements ImageSampledFailListener {

    public static final String collection = "Dy_FileImageGetGet";

    public FileImageGetGetSampler() {
        Publisher.subscribe(this);
    }

    @Override
    public List<GroupFail<FileImageGetGet.Param>> doSample(List<Param<FileImageGetGet.Param>> params) {
        
        Consumer<FileImageGetGet.Param, List<FileImageGetV2ResponseDataListInner>> consumer = (context, param, list) -> {
            List<UpdateOneModel<Document>> documents = list
                    .stream()
                    .map(data -> new MongoRowBuilder<>(data)
                                                .removeNull()
                                                .id(data.getId())
                                                .append("advertiserId", param.getAdvertiserId())
                                                .build()
                    )
                    .collect(Collectors.toList());
                MongoStore.store(collection, documents);
        };

        return new PageGroup<>(SamplerUtil.page(FileImageGetGet.class), params, consumer).execute();
    }

    @Override
    protected void postProcess() {

        // Publisher.publish(new ImageSampledEvent(Platform.Oceanengine));
    }

    @Override
    public void onImageSampledFail(ImageSampledFailEvent event) {
        if (Platform.Oceanengine == event.getPlatform()) {
            List<PlatformImage> list = event.getList();
            List<DyAdvertiserID> advertisers = MongoStore.list(DyAdvertiserID.class, AdvertiserGetApiSampler.collection);
            Map<Long, Long> advertiserID2AppId = advertisers.stream().collect(Collectors.toMap(DyAdvertiserID::getAdvertiserId, DyAdvertiserID::getAppId));
            Map<Long, List<PlatformImage>> map = list.stream().collect(Collectors.groupingBy(PlatformImage::getAccountId));
            List<Param<FileImageGetGet.Param>> params = new ArrayList<>();
            for (Long advertiserId : map.keySet()) {
                List<PlatformImage> ids = map.get(advertiserId);
                Long appId = advertiserID2AppId.get(advertiserId);
                if (null == appId) {
                    continue;
                }
                List<List<PlatformImage>> partitions = Lists.partition(ids, 80);
                for (List<PlatformImage> partition : partitions) {
                    FileImageGetGet.Param p = new FileImageGetGet.Param();
                    p.setAdvertiserId(advertiserId);
                    FileImageGetV2Filtering filter = new FileImageGetV2Filtering();
                    for (String id : partition.stream().map(PlatformImage::getPlatformId).collect(Collectors.toList())) {
                        filter.addImageIdsItem(id);
                    }
                    p.setFiltering(filter);
                    params.add(new Param<>(new DyContextImpl(appId), p));
                }
            }
            this.sample(params);
        }
    }
}
