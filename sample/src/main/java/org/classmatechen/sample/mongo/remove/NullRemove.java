package org.classmatechen.sample.mongo.remove;

import java.util.Objects;

import org.classmatechen.sample.mongo.Remove;

public class NullRemove implements Remove {

    @Override
    public boolean process(String key, Object value) {

        return Objects.isNull(value);
    }
}
