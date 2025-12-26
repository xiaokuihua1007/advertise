package org.classmatechen.sample.po;

import lombok.Data;

@Data
public class PlatformImage {

    private Long id;
    private String platformId;
    private Integer platform;
    private String filename;
    private String url;
    /**
     * 本地路径
     */
    private String path;
}
