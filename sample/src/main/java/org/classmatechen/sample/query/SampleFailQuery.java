package org.classmatechen.sample.query;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SampleFailQuery extends Page {

    private Date startOccurTime;
    private Date endOccurTime;
    private Boolean isDelete;
    private String sampler;
}
