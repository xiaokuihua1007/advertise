package org.classmatechen.core.common;

import lombok.Data;

@Data
public class Page {

    private Long pageSize;
    private Long page;

    {
        pageSize = 10L;
        page = 1L;
    }
}
