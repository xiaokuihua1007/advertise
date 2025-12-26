package org.classmatechen.sample.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PlatformVideoQuery extends Page {

    private Integer platform;
    private boolean pathExist;
}
