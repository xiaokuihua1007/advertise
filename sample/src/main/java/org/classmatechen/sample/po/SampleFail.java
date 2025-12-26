package org.classmatechen.sample.po;

import java.util.Date;

import lombok.Data;

@Data
public class SampleFail {

    private Long id;
    private String sampler;
    private String param;
    private String paramClass;
    private String context;
    private String contextClass;
    private String error;
    private Date occurTime;
    private Integer finish;
}
