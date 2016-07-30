package com.qg.smpt.printer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tisong on 7/29/16.
 */
public final class StatusMap {

    public static final Map<Integer, String> printerStatusMap = new HashMap<>();

    static {
        printerStatusMap.put(0x01, "切刀错误");
        printerStatusMap.put(0x02, "机盒打开");
        printerStatusMap.put(0x03, "纸将用尽");
        printerStatusMap.put(0x04, "正在进纸");
        printerStatusMap.put(0x05, "机芯高温");
        printerStatusMap.put(0x06, "机芯烧毁");
        printerStatusMap.put(0x07, "正常状态");
        printerStatusMap.put(0x08, "待定");
        printerStatusMap.put(0x09, "待定");
        printerStatusMap.put(0x0A, "待定");
        printerStatusMap.put(0x0B, "待定 ");
        printerStatusMap.put(0x0C, "普通缓冲区满");
        printerStatusMap.put(0x0D, "加急缓冲区满");
        printerStatusMap.put(0x0E, "健康状态");
        printerStatusMap.put(0x0F, "亚健康状态");
    }

}
