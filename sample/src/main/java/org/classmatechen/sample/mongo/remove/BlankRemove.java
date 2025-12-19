package org.classmatechen.sample.mongo.remove;

import java.util.Objects;

import org.classmatechen.sample.mongo.Remove;

public class BlankRemove implements Remove {

    @Override
    public boolean process(String key, Object value) {

        if (Objects.nonNull(value) && value instanceof CharSequence) {
            CharSequence sequence = (CharSequence) value;
            return sequence.length() == 0;
        }
        return false;
    }
}
