package com.qg.smpt.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import com.qg.smpt.web.model.User;
import com.qg.smpt.web.repository.UserMapper;
import com.qg.smpt.web.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	private static final Logger lOGGER = Logger.getLogger(UserServiceImpl.class);
	
	
	@Resource
	private UserMapper userMapper;	
	/**
	 * 根据用户id获取用户
	 * @param userId
	 * @return 存在则返回对应用户,不存在则返回空
	 */
	public User queryById(int userId) {
		return userMapper.selectByPrimaryKey(userId);
	}
	
	/**
	 * 查询所有用户
	 * @return 返回对应的用户集合
	 */
	public List<User> queryAllUser() {
		List<User> users = null;
		try{
			users = userMapper.selectAllUser();
		}catch(Exception e) {
			lOGGER.log(Level.ERROR, "UserService.queryAllUser 查询出错 ", e);
		}
		
		return users;
	}
	
	
}
