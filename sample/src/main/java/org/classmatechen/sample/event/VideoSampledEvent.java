package org.classmatechen.sample.event;

import org.classmatechen.basic.pubsub.Event;
import org.classmatechen.basic.pubsub.Listener;
import org.classmatechen.common.Platform;

import lombok.Getter;

public class VideoSampledEvent extends Event {

    @Getter
    private final Platform platform;

    public VideoSampledEvent(Platform platform) {
        super(null);
        this.platform = platform;
    }

    @Override
    public void publish(Listener var1) {

        if (var1 instanceof VideoSampledListener) {
            VideoSampledListener listener = (VideoSampledListener) var1;
            listener.onVideoSampled(this);
        }
    }
}
