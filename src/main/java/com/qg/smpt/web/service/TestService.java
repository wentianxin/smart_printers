package com.qg.smpt.web.service;

import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.model.User;
import com.qg.smpt.web.repository.OrderMapper;
import com.qg.smpt.web.repository.UserMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by tisong on 7/24/16.
 */

@Component
public class TestService {



    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UserMapper userMapper;

    public int insertOrder(Order order) {


        order = this.orderMapper.selectByPrimaryKey(new Integer(1));

        User user = this.userMapper.selectByPrimaryKey(1);

        return 1;
    }


}
