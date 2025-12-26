package org.classmatechen.sample.event;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.classmatechen.basic.group.GroupFail;
import org.classmatechen.basic.pubsub.Listener;
import org.classmatechen.basic.pubsub.Publisher;
import org.classmatechen.sample.po.SampleFail;
import org.classmatechen.sample.service.SampleFailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class SampleItemFailEventListener implements Listener {

    @Autowired
    private SampleFailServiceImpl service;

    public SampleItemFailEventListener() {
        Publisher.subscribe(this);
    }

    public void onSampleItemFail(String sampler, List<GroupFail<?>> params) {

        if (null == params || params.isEmpty()) {
            return;
        }
        List<SampleFail> fails = params.stream().map(param -> {
            SampleFail fail = new SampleFail();
            fail.setSampler(sampler);
            if (null != param.getParam()) {
                fail.setParam(new Gson().toJson(param.getParam()));
                fail.setParamClass(param.getParam().getClass().getName());
            }
            fail.setContext(new Gson().toJson(param.getContext()));
            fail.setContextClass(param.getContext().getClass().getName());
            fail.setError(param.getError());
            fail.setOccurTime(new Date());
            fail.setFinish(0);
            return fail;
        }).collect(Collectors.toList());

        service.batchInsert(fails);
    }
}
