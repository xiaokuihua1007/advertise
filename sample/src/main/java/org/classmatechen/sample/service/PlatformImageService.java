package org.classmatechen.sample.service;

import java.util.List;

import org.classmatechen.basic.pubsub.Publisher;
import org.classmatechen.common.Platform;
import org.classmatechen.sample.event.ImageSampledEvent;
import org.classmatechen.sample.event.ImageSampledFailEvent;
import org.classmatechen.sample.event.ImageSampledListener;
import org.classmatechen.sample.mapper.PlatformImageMapper;
import org.classmatechen.sample.po.PlatformImage;
import org.classmatechen.sample.query.PlatformImageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlatformImageService implements ImageSampledListener {

    @Autowired
    private PlatformImageMapper mapper;

    @Autowired
    private HttpService httpService;

    @Autowired
    private FileService fileService;

    public PlatformImageService() {
        Publisher.subscribe(this);
    }

    public void loadResource(Platform platform) {

        Long page = 1L;
        Long limit = 100L;
        PlatformImageQuery query = new PlatformImageQuery();
        query.setPlatform(platform.getId());
        query.setPathExist(false);
        query.setPage(page);
        query.setLimit(limit);
        List<PlatformImage> list = mapper.list(query);

        while (true) {
            for (PlatformImage image : list) {
                if (null != image.getPath()) {
                    continue;
                }
                try {
                    download(image);
                } catch (Exception E) {
                    
                }
            }
            if (list.size() < limit) {
                break;
            } else {
                query.next();
                list = mapper.list(query);
            }
        }

        list = mapper.list(query);
        if (list.size() > 0) {
            Publisher.publish(new ImageSampledFailEvent(Platform.Oceanengine, list));
        }
    }

    private void download(PlatformImage image) throws Exception {
        
        String url = image.getUrl();
        String filename = image.getName();
        if (null == url || null == filename) {
            return;
        }
        int index = filename.lastIndexOf('.');
        String suffix = index < 0 ? null : filename.substring(index);
        String path = httpService.download(url, suffix, fileService);
        image.setPath(path);
        mapper.updatePath(image);
    }

    @Override
    public void onImageSampled(ImageSampledEvent event) {

        Platform platform = event.getPlatform();
        loadResource(platform);
    }
}
