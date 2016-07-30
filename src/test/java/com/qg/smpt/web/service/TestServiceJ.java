package com.qg.smpt.web.service;

/**
 * Created by tisong on 7/25/16.
 */


import com.qg.smpt.web.model.Printer;
import com.qg.smpt.web.repository.PrinterMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:spring/spring-*.xml"})

public class TestServiceJ {


    @Resource
    private TestService testService;

    @Resource
    private PrinterMapper printerMapper;

    @Test
    public void test1() {
        Printer printer = printerMapper.selectPrinter(1);
        
    }

}
