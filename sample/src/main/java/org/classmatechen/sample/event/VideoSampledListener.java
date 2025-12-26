package org.classmatechen.sample.event;

import org.classmatechen.basic.pubsub.Listener;

public interface VideoSampledListener extends Listener {

    void onVideoSampled(VideoSampledEvent event);
}
