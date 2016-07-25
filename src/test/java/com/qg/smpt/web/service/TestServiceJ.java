package com.qg.smpt.web.service;

/**
 * Created by tisong on 7/25/16.
 */


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})

public class TestServiceJ {


    @Resource
    private TestService testService;

    @Test
    public void test1() {
        testService.insertOrder(null);
    }

}
