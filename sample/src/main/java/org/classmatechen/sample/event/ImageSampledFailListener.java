package org.classmatechen.sample.event;

import org.classmatechen.basic.pubsub.Listener;

public interface ImageSampledFailListener extends Listener {

    void onImageSampledFail(ImageSampledFailEvent event);
}
