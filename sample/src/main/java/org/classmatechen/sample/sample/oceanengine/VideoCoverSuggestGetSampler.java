package org.classmatechen.sample.sample.oceanengine;

import java.util.List;

import org.classmatechen.basic.group.Param;
import org.classmatechen.sample.sample.Sampler;
import org.springframework.stereotype.Service;

@Service
public class VideoCoverSuggestGetSampler implements Sampler<Void> {

    @Override
    public void sample(List<Param<Void>> params) {
        throw new UnsupportedOperationException("Unimplemented method 'sample'");
    }
}
