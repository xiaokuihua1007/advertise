package org.classmatechen.sample.service;

import java.util.List;

import org.classmatechen.basic.pubsub.Publisher;
import org.classmatechen.common.Platform;
import org.classmatechen.sample.event.VideoSampledEvent;
import org.classmatechen.sample.event.VideoSampledListener;
import org.classmatechen.sample.mapper.PlatformVideoMapper;
import org.classmatechen.sample.po.PlatformVideo;
import org.classmatechen.sample.query.PlatformVideoQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlatformVideoService implements VideoSampledListener {

    @Autowired
    private PlatformVideoMapper mapper;

    @Autowired
    private HttpService httpService;

    @Autowired
    private FileService fileService;

    public PlatformVideoService() {
        Publisher.subscribe(this);
    }

    public void loadResource(Platform platform) {

        Long page = 1L;
        Long limit = 100L;
        PlatformVideoQuery query = new PlatformVideoQuery();
        query.setPlatform(platform.getId());
        query.setPathExist(false);
        query.setPage(page);
        query.setLimit(limit);
        List<PlatformVideo> list = mapper.list(query);

        while (true) {

            for (PlatformVideo video : list) {
                if (null != video.getPath()) {
                    continue;
                }
                try {
                    download(video);
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
    }

    private void download(PlatformVideo video) throws Exception {
        
        String url = video.getUrl();
        String filename = video.getFilename();
        if (null == url || null == filename) {
            return;
        }
        int index = filename.lastIndexOf('.');
        String suffix = index < 0 ? null : filename.substring(index);
        String path = httpService.download(url, suffix, fileService);
        video.setPath(path);
        if (null != video.getPosterUrl()) {
            video.setPostPath(httpService.download(video.getPosterUrl(), null, fileService));;
        }
        mapper.updatePath(video);
    }

    @Override
    public void onVideoSampled(VideoSampledEvent event) {

        Platform platform = event.getPlatform();
        loadResource(platform);
    }
}
