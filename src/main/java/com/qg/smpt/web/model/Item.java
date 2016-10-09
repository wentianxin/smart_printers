package com.qg.smpt.web.model;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 订单内容
 */

public class Item {
    private String name;
    @JsonIgnore
    private int price;
    private int count;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public int getCost() {
        return price * count;
    }

    @Override
    public String toString() {
        return getName() +"   X" + getCount() + "        " + getCost();
    }
}


