package com.qg.smpt.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by asus2015 on 2016/10/8.
 */
public class TimeUtil {
    private static SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd hh:mm");

    public static String timeToString(long time) {
        return format.format(new Date(time));
    }
}
