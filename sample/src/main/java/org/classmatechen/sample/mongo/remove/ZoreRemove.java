package org.classmatechen.sample.mongo.remove;

import java.util.Objects;

import org.classmatechen.sample.mongo.Remove;

public class ZoreRemove implements Remove {

    @Override
    public boolean process(String key, Object value) {

        if (Objects.nonNull(value) && value instanceof Number) {
            Number number = (Number) value;
            return number.doubleValue() == 0.0;
        }
        return false;
    }
}
