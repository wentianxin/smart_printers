package com.qg.smpt.web.service;

import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.repository.OrderMapper;

import javax.annotation.Resource;

/**
 * Created by tisong on 7/24/16.
 */


public class TestService {



    @Resource
    private OrderMapper orderMapper;


    public int insertOrder(Order order) {


        order = this.orderMapper.selectByPrimaryKey(new Integer(1));

        return 1;
    }


}
