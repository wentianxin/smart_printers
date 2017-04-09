package com.qg.smpt.printer.util;

/**
 * Created by tisong on 3/29/17.
 */
public class FormatUtil {

    public static String getOrderStatus(byte flag) {
        String status = null;
        switch (flag) {
            case (byte)0: status = "普通订单打印成功"; break;
            case (byte)1: status = "普通订单打印出错：打印机异常"; break;
            case (byte)2: status = "普通订单进入打印队列"; break;
            case (byte)3: status = "普通订单开始打印"; break;
            case (byte)4: status = "普通订单订单数据解析错误"; break;
            case (byte)5: status = "异常订单打印成功"; break;
            case (byte)6: status = "异常订单打印出错：打印机异常"; break;
            case (byte)7: status = "异常订单进入打印队列"; break;
            case (byte)8: status = "异常订单开始打印"; break;
            case (byte)9: status = "异常订单数据解析错误"; break;
        }
        return status;
    }
}
