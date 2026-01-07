package org.classmatechen.sample.po;

import lombok.Data;

@Data
public class PlatformVideo {

    private Long id;
    private String platformId;
    private String name;
    private Integer platform;
    private String url;
    /**
     * 本地路径
     */
    private String path;
    private String posterUrl;
    /**
     * 本地封面路径
     */
    private String postPath;
    private Long accountId;
}
