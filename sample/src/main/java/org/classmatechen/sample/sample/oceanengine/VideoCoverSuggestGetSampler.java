package org.classmatechen.sample.sample.oceanengine;

import java.util.List;

import org.classmatechen.basic.group.GroupFail;
import org.classmatechen.basic.group.Param;
import org.classmatechen.sample.sample.AbstarctSampler;
import org.springframework.stereotype.Service;

@Service
public class VideoCoverSuggestGetSampler extends AbstarctSampler<Void> {

    @Override
    public List<GroupFail<Void>> doSample(List<Param<Void>> params) {
        throw new UnsupportedOperationException("Unimplemented method 'sample'");
    }
}
