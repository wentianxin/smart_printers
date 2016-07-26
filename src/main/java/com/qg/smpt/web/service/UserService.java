package com.qg.smpt.web.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qg.smpt.web.model.User;
import com.qg.smpt.web.repository.UserMapper;

@Service
public class UserService {
	
	@Resource
	private UserMapper userMapper;
	
	public User queryById(int userId) {
		return userMapper.selectByPrimaryKey(userId);
	}
}
