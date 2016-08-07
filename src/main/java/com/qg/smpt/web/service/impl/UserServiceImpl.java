package com.qg.smpt.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.print.attribute.standard.NumberUp;

import com.qg.smpt.share.ShareMem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import com.qg.smpt.web.model.Constant;
import com.qg.smpt.web.model.Printer;
import com.qg.smpt.web.model.User;
import com.qg.smpt.web.repository.PrinterMapper;
import com.qg.smpt.web.repository.UserMapper;
import com.qg.smpt.web.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);
	
	
	@Autowired
	private UserMapper userMapper;	
	
	@Autowired
	private PrinterMapper printerMapper;
	
	/**
	 * 根据用户id获取用户
	 * @param userId
	 * @return 存在则返回对应用户,不存在则返回空
	 */
	public User queryById(int userId) {
		LOGGER.log(Level.DEBUG, "正在通过主键[{0}]来查找用户", userId);
		
		User user = null;
		try{
			user = userMapper.selectByPrimaryKey(userId);
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "通过主键[{0}]来查找用户出错了", userId,e);
		} 
		
		return user;
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
			LOGGER.log(Level.ERROR, "UserService.queryAllUser 查询所有用户出错 ", e);
		}
		
		return users;
	}
	

	@Transactional(rollbackFor=Exception.class)
	public int registerUser(User user) throws RuntimeException {
		try{
			//执行插入用户
			int userId = userMapper.insert(user);
			
			List<Printer> printers = user.getPrinters();
			for(Printer p : printers) {
				p.setUserId(userId);
				p.setPrinterStatus(String.valueOf((int)(Constant.PRINTER_HEATHY)));
			}
			
			printerMapper.insertPrinterBatch(printers);
			printerMapper.addUserPrinterBatch(printers);
			
			return Constant.TRUE;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "注册用户失败了", e);
			
			throw(new RuntimeException("注册用户时出现了错误"));
		}
	}
	
	public User login(User user) {
		User loginUser = null;
		try{
			
			loginUser = userMapper.selectByLogin(user);

			if(loginUser != null && loginUser.getId() > 0) {
				loginUser = userMapper.selectUserPrinter(loginUser.getId());

			}

		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "userService.login(),用户登录时出现错误", e);
		}
		
		return loginUser;
	}
	
	
	public User queryUserPrinter(int userId) {
		LOGGER.log(Level.DEBUG, "正在查询用户[{0}]的打印机", userId);
		
		User user = userMapper.selectUserPrinter(userId);

		LOGGER.log(Level.DEBUG, "用户 [{0}] 拥有 [{1}] 台打印机", userId, (user != null && user.getPrinters() != null) ? user.getPrinters().size() : 0);
	
		return user;
				
	}

	@Override
	public String updateLogo(String path, int userId) {
		LOGGER.log(Level.INFO,"用户[{0}]正在更新logo信息，路径为[{1}]", userId,path);
		User u = new User();
		u.setId(userId);
		u.setUserLogo(path);
		String status =  userMapper.updateLogo(u) > 0 ? Constant.SUCCESS : Constant.ERROR;

		if(status.equals(Constant.SUCCESS)) {
			User user = ShareMem.userIdMap.get(userId);
			user.setUserLogo(path);
			user.setConvert(false);
		}

		return status;
	}

}
