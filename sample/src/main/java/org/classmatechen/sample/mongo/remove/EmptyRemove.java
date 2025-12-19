package org.classmatechen.sample.mongo.remove;

import java.util.Collection;
import java.util.Objects;

import org.classmatechen.sample.mongo.Remove;

public class EmptyRemove implements Remove {

    @Override
    public boolean process(String key, Object value) {
        if (Objects.nonNull(value) && value instanceof Collection) {
            return ((Collection<?>) value).isEmpty();
        }
        return false;
    }
}
