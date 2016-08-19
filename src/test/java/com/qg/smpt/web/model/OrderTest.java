package com.qg.smpt.web.model;

import com.qg.smpt.printer.model.BOrder;
import com.qg.smpt.share.ShareMem;
import com.qg.smpt.util.DebugUtil;
import com.qg.smpt.util.OrderBuilder;
import com.qg.smpt.web.service.UserService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.UnsupportedEncodingException;

/**
 * Created by asus2015 on 2016/8/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/*.xml"})
public class OrderTest {
    @Autowired
    private UserService userService;


    @Test
    public void testConvert() throws UnsupportedEncodingException {
        String a = "美团外卖\n";
        StringBuffer b = new StringBuffer();
        b.append("美团外卖\n");
        byte[] ab = a.getBytes("gb2312");

        for(int i = 0; i < ab.length; i++) {
            System.out.println(Integer.toHexString(ab[i] & 0xFF));
        }


        byte[] bb = b.toString().getBytes("gb2312");
        for(int i = 0; i < bb.length; i++) {
            System.out.println(Integer.toHexString(bb[i] & 0xFF));
        }

    }
}
