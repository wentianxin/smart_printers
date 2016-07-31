package com.qg.smpt.web.processor;

import static org.junit.Assert.*; 

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qg.smpt.util.JsonUtil;
import com.qg.smpt.web.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/*.xml"})

public class TestUserController {

	@Autowired
	private UserController userController;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		StringBuffer buffer = new StringBuffer();
//		buffer.append("{\"userName\":\"李伟淙\",");
//		buffer.append("\"userAccount\":\"111111\",");
//		buffer.append("\"userPassword\":\"12580\",");
//		buffer.append("\"userStore\":\"麦当劳\",");
//		buffer.append("\"userAddress\":\"gogo新天地\",");
//		buffer.append("\"userPhone\":\"18819255400\",");
//		buffer.append("\"printers\":");
//		buffer.append("[{\"id\":\"27\"},{\"id\":\"28\"}]}");
//		System.out.println(buffer.toString());
		buffer.append("{\"printerId\":[{\"id\":\"75\"}],\"userAccount\":\"5\",\"userAddress\":\"f\",\"userName\":\"gh\",\"userPassword\":\"f\",\"userPhone\":\"4\",\"userStore\":\"t\"}");
		User user = (User)JsonUtil.jsonToObject(buffer.toString(), User.class);
//		String status = userController.register(buffer.toString());
		System.out.println("");
	}
//	{"printerId":[{"id":"75"}],"userAccount":"5","userAddress":"f","userName":"gh","userPassword":"f","userPhone":"4","userStore":"t"}
}
