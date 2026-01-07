package org.classmatechen.sample.event;

import java.util.List;

import org.classmatechen.basic.pubsub.Event;
import org.classmatechen.basic.pubsub.Listener;
import org.classmatechen.common.Platform;
import org.classmatechen.sample.po.PlatformImage;

import lombok.Getter;

public class ImageSampledFailEvent extends Event {

    @Getter
    private final Platform platform;
    @Getter
    private final List<PlatformImage> list;

    public ImageSampledFailEvent(Platform platform, List<PlatformImage> list) {
        super(null);
        this.platform = platform;
        this.list = list;
    }

    @Override
    public void publish(Listener var1) {

        if (var1 instanceof ImageSampledFailListener) {
            ImageSampledFailListener listener = (ImageSampledFailListener) var1;
            listener.onImageSampledFail(this);
        }
    }
}
