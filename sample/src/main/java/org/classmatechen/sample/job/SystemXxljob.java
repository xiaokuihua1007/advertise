package org.classmatechen.sample.job;

import org.classmatechen.sample.service.SampleFailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xxl.job.core.handler.annotation.XxlJob;

@Component
public class SystemXxljob {

    @Autowired
    private SampleFailServiceImpl failService;

    /**
     * 拉取昨天全部基础数据
     */
    @XxlJob("recoverSampleFail")
    public void recoverSampleFail() {
        failService.retry();
    }
}
