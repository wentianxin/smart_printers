package com.qg.smpt.web.service;

import com.qg.smpt.web.model.test.Order;
import com.qg.smpt.web.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by tisong on 7/24/16.
 */

@Service
public class TestService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private OrderMapper orderMapper;


    public int insertOrder(Order order) {

        System.out.println(this.orderMapper.selectByPrimaryKey(1));

        return 1;
    }


}
