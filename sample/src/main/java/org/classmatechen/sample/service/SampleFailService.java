package org.classmatechen.sample.service;

import java.util.List;

import org.classmatechen.sample.po.SampleFail;

public interface SampleFailService {

    void batchInsert(List<SampleFail> fails);

    void retry();
}
