package com.an.base.model;

/**
 * Created by sunshuntao 2016.09.19,枚举定义参考
 */
public enum ResponseDayNightModel {

    DAY("DAY", 0),
    NIGHT("NIGHT", 1);

    private String name;
    private int code;

    private ResponseDayNightModel(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
