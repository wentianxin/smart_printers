package com.qg.smpt.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tisong on 7/26/16.
 */
public class BytesConvertTest {

    @Test
    public void bytesToShort() throws Exception {

    }

    @Test
    public void bytesToInt() throws Exception {

    }

    @Test
    public void intToBytes() throws Exception {

    }

    @Test
    public void shortToBytes() throws Exception {

    }

    @Test
    public void fillShort() throws Exception {
        byte[] bytes = new byte[2];

        BytesConvert.fillShort((short)0x0001, bytes, 0);

        System.out.println(bytes.toString());
        System.out.println(bytes[0] + "" + bytes[1]);
    }

    @Test
    public void fillInt() throws Exception {

    }

    @Test
    public void fillByte() throws Exception {

    }

}