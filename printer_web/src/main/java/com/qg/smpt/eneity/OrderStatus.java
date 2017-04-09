package com.qg.smpt.eneity;

/**
 * Created by tisong on 4/8/17.
 */
public enum OrderStatus {

    Typed(100),

    Typing(120),

    NonTyping(130),

    TypeException(110);

    private final int value;

    // 构造器默认也只能是private, 从而保证构造函数只能在内部使用
    OrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
