package org.classmatechen.core.common;

public class CEnum {

    private final int code;
    private final String lable;

    protected CEnum(int code, String lable) {
        this.code = code;
        this.lable = lable;
    }

    public int code() {
        return this.code;
    }

    public String label() {
        return this.lable;
    }
}
