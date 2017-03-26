package com.qg.smpt.web.processor;

import com.qg.smpt.web.model.Printer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by asus2015 on 2016/11/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/*.xml"})
public class TestPrinterController {

    @Autowired
    private LoginController loginController;
    @Autowired
    private PrinterController printerController;

    @Test
    public void testPrinterStatus() {
        loginController.login("1", "1",new MockHttpServletRequest(), new MockHttpServletResponse());

        System.out.println(printerController.queryPrinter(1));
    }
}
