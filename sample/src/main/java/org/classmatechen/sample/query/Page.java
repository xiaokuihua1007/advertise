package org.classmatechen.sample.query;

import lombok.Data;

@Data
public class Page {

    private Long limit;
    private Long page;

    public void next() {
        this.page++;
    }
}
