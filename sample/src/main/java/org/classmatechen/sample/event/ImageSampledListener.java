package org.classmatechen.sample.event;

import org.classmatechen.basic.pubsub.Listener;

public interface ImageSampledListener extends Listener {

    void onImageSampled(ImageSampledEvent event);
}
