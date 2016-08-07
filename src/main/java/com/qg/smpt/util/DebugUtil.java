package com.qg.smpt.util;

import java.util.Arrays;

/**
 * 测试专用工具
 */
public final class DebugUtil {

    private final static Logger LOGGER = Logger.getLogger(DebugUtil.class);
    /**
     * 打印字节数组，每行32个字节
     * @param bytes
     */
    public static void printBytes(byte[] bytes) {

        for (int i = 0; i < bytes.length; i += 4) {
            byte[] test = new byte[4];
            System.arraycopy(bytes, i, test, 0, 4);
            StringBuffer stringBuffer = new StringBuffer();
            for (int j = 0; j < 4; j++)
                stringBuffer.append(Integer.toHexString(test[j] & 0xFF) + ",");

            //System.out.print(stringBuffer.toString());

            LOGGER.log(Level.DEBUG, "第[{0}]字节 ： [{1}]", i, stringBuffer.toString());
        }


    }
}
