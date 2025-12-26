package org.classmatechen.sample.event;

import org.classmatechen.basic.pubsub.Event;
import org.classmatechen.basic.pubsub.Listener;
import org.classmatechen.common.Platform;

import lombok.Getter;

public class ImageSampledEvent extends Event {

    @Getter
    private final Platform platform;

    public ImageSampledEvent(Platform platform) {
        super(null);
        this.platform = platform;
    }

    @Override
    public void publish(Listener var1) {

        if (var1 instanceof ImageSampledListener) {
            ImageSampledListener listener = (ImageSampledListener) var1;
            listener.onImageSampled(this);
        }
    }
}
