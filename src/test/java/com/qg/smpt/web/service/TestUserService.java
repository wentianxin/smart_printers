package com.qg.smpt.web.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qg.smpt.web.model.Printer;
import com.qg.smpt.web.model.User;
import com.qg.smpt.web.service.impl.UserServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:spring/spring-*.xml"})

public class TestUserService {

	@Resource
	private UserServiceImpl userService;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void test() {
		User u = userService.queryById(1);
		System.out.println(u.getUserName());
	}
	@Ignore
	@Test
	public void testQueryAll() {
		List<User> users = userService.queryAllUser();
		System.out.println(users.size());
		
		for(User user : users) {
			System.out.println(user.getUserName());
		}
	}
	
	@Ignore
	@Test
	public void testRegister() {
		User user = new User();
		user.setUserName("梁碧如");
		user.setUserAccount("12580");
		user.setUserPassword("123456");
		user.setUserStore("bill bill 俱乐部");
		user.setUserAddress("华农农业大学");
		user.setUserPhone("18819255400");
		user.setUserLogo("");
		user.setUserQrcode("");
		List<Printer> printers = new ArrayList<>();
		for(int i = 6; i < 10; i++) {
			
			Printer p = new Printer();
			p.setId(i);
			printers.add(p);
		}
		
		user.setPrinters(printers);
		user.setUserPrinters(printers.size());
		try{
			userService.registerUser(user);
		}catch(RuntimeException exception) {
			
		}
		
		
	}

}
