package org.classmatechen.sample.event;

import org.classmatechen.basic.pubsub.Listener;

public interface VideoSampledFailListener extends Listener {

    void onVideoSampledFail(VideoSampledFailEvent event);
}
