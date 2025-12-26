package org.classmatechen.sample.sample;

import java.util.List;

import org.classmatechen.basic.group.GroupFail;
import org.classmatechen.basic.group.Param;
import org.classmatechen.basic.pubsub.Publisher;
import org.classmatechen.sample.event.SampleItemFailEvent;

public abstract class AbstarctSampler<P> implements Sampler<P> {

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void sample(List<Param<P>> params) {

        List<GroupFail<P>> fail = doSample(params);
        if (null != fail && !fail.isEmpty()) {
            Publisher.publish(new SampleItemFailEvent(this.getClass(), (List) fail));
        }
        this.postProcess();
    }

    protected void postProcess() { }

    protected abstract List<GroupFail<P>> doSample(List<Param<P>> params);
}
