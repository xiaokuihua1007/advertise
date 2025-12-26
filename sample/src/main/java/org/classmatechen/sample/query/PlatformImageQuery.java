package org.classmatechen.sample.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PlatformImageQuery extends Page {

    private Integer platform;
    private Boolean pathExist;
}
