package org.classmatechen.sample.sample;

import java.util.Arrays;
import java.util.List;

import org.classmatechen.basic.group.Param;

public interface Sampler<P> {

    void sample(List<Param<P>> params);

    default void sample(Param<P> param) {
        sample(Arrays.asList(param));
    }
}
