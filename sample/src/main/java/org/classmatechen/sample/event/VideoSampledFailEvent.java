package org.classmatechen.sample.event;

import java.util.List;

import org.classmatechen.basic.pubsub.Event;
import org.classmatechen.basic.pubsub.Listener;
import org.classmatechen.common.Platform;
import org.classmatechen.sample.po.PlatformVideo;

import lombok.Getter;

public class VideoSampledFailEvent extends Event {

    @Getter
    private final Platform platform;
    @Getter
    private final List<PlatformVideo> list;

    public VideoSampledFailEvent(Platform platform, List<PlatformVideo> list) {
        super(null);
        this.platform = platform;
        this.list = list;
    }

    @Override
    public void publish(Listener var1) {

        if (var1 instanceof VideoSampledFailListener) {
            VideoSampledFailListener listener = (VideoSampledFailListener) var1;
            listener.onVideoSampledFail(this);
        }
    }
}
