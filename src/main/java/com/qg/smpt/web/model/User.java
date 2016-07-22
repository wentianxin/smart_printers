package com.qg.smpt.web.model;

import java.util.List;

/**
 * 商家用户
 * Created by tisong on 7/20/16.
 */
public final class User {
    private int id;
    private String name;
    private int mpu;
    private List<Printer> printers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMpu() {
        return mpu;
    }

    public void setMpu(int mpu) {
        this.mpu = mpu;
    }

    public List<Printer> getPrinters() {
        return printers;
    }

    public void setPrinters(List<Printer> printers) {
        this.printers = printers;
    }

}
