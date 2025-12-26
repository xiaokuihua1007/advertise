package org.classmatechen.sample.event;

import java.util.List;

import org.classmatechen.basic.group.GroupFail;
import org.classmatechen.basic.pubsub.Event;
import org.classmatechen.basic.pubsub.Listener;

public class SampleItemFailEvent extends Event {

    private final String sampler;
    private final List<GroupFail<?>> params;

    public SampleItemFailEvent(Class<?> sampler, List<GroupFail<?>> params) {
        super(null);
        this.sampler = sampler.getName();
        this.params = params;
    }

    @Override
    public void publish(Listener var1) {

        if (var1 instanceof SampleItemFailEventListener) {
            SampleItemFailEventListener listener = (SampleItemFailEventListener) var1;
            listener.onSampleItemFail(this.sampler, this.params);
        }
    }
}
