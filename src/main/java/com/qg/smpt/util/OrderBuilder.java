package com.qg.smpt.util;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.qg.smpt.web.model.Item;
import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.model.User;
import com.qg.smpt.web.service.UserService;

public class OrderBuilder {
	private static final Logger LOGGER = Logger.getLogger(OrderBuilder.class);
	
	
	private UserService userService;
	
	private List<User> users = null;
	private int userCount = 0;
	
	
	public OrderBuilder(UserService userService) {
		this.userService = userService;
		init();
	}
	
	private void init(){
		LOGGER.log(Level.DEBUG, "订单生成器正在初始化商家信息", OrderBuilder.class);
		users = userService.queryAllUser();
		if(users != null)
			userCount = users.size();
		else
			users = new ArrayList<>();
	}
	
	
	private static int num = 0;
	
	//公司名称
	private static String[] companys = {"美团外卖", "饿了吗", "百度淘米", "百度外卖"};
	
//	//商家信息
//	private static String shops[] = {"麦当劳", "肯德基", "好难吃的地方", "想不出什么地方了"};
//	private static String address[] = {"gogo新天地一楼25号","广大商业区", "广东工业大学饭堂", "地址未知" };
//	private static String contact[] = {"85241523", "84523651", "15521232546", "15622365842"};
	
	//菜单
	private static String dish[] = {"西红柿炒番茄", "葡萄炒木耳", "西瓜炒香蕉", "榴莲鸡蛋", "童子鸡"};
	private static int prices[] = {4, 4, 5, 3, 8};
	
	//顾客信息
	private static String customers[] = {"陈俊铭", "温天信", "张诗婷", "李伟淙", "许艺茂", "方锐"};
	private static String cAddress[] = {"广工西三736", "广工西四612", "广工东十三xxx", "广工西三741", "广工西三747", "广工西三2xx"};
	private static String cContact[] = {"15521256251", "18852423652", "13432252452", "15622365455", "18819255400", "15695542562"};
	
	private static String remarks[] = {"加饭", "晚点来", "加菜"};
	
	private static String expectTimes[] = {"10:30", "11.30", "12.30", "5.30", "6.30", "7.30"};
	
//	public Order produceOrder() throws UnsupportedEncodingException {
//		Order order = new Order();
//		
//		int randomNum = 0;
//		
//		//生活公司信息
//		randomNum = getRandom(4);
//		order.setCompany(companys[randomNum]);
//		
//		//生成商家信息
//		randomNum = getRandom(4);
//		order.setShopName(shops[randomNum]);
//		order.setFrom(address[randomNum]);
//		order.setShopContact(contact[randomNum]);
//		
//		//获取订单信息
//		order.setOrderNum(getOrderNum());
//		order.setOrderTime(new Date().toString());
//		order.setExpectTime(expectTimes[getRandom(6)]);
//		order.setRemark(remarks[getRandom(3)]);
//		
//		//生成顾客信息
//		randomNum = getRandom(6);
//		order.setCustomer(customers[randomNum]);
//		order.setTo(cAddress[randomNum]);
//		order.setComContact(cContact[randomNum]);
//		
//		//生成菜
//		randomNum = getRandom(5) + 1;
//		List<Item> items = new ArrayList<>(randomNum);
//		for(int i = 0; i < randomNum; i++){
//			items.add(createItem(i));
//		}
//		order.setItems(items);
//		
//		//生成其他付费信息
//		order.setMealCost(getMealCost());
//		order.setDeliveryCost(getdeliveryCost());
//		
//		return order;
//	}
	
	public Order produceOrder()  {
		Order order = new Order();
		
		int randomNum = 0;
		
		//生活公司信息
		randomNum = getRandom(4);
		order.setCompany(companys[randomNum]);
		
		//生成商家信息
//		randomNum = getRandom(4);
		if(userCount > 0){
			randomNum = getRandom(userCount);
			User u = users.get(randomNum);
			order.setClientName(u.getUserStore());
			order.setClientAddress(u.getUserAddress());
			order.setClientTelephone(u.getUserPhone());
		}
		
		//获取订单信息
		order.setId(getOrderNum());
		order.setOrderTime((new Date()));
		order.setExpectTime(expectTimes[getRandom(6)]);
		order.setOrderRemark(remarks[getRandom(3)]);
		
		//生成顾客信息
		randomNum = getRandom(6);
		order.setUserName(customers[randomNum]);
		order.setUserAddress(cAddress[randomNum]);
		order.setUserTelephone(cContact[randomNum]);
		
		//生成菜
		randomNum = getRandom(5) + 1;
		List<Item> items = new ArrayList<Item>(randomNum);
		for(int i = 0; i < randomNum; i++){
			items.add(createItem(i));
		}
		order.setItems(items);
		
		//生成其他付费信息
		order.setOrderMealFee(getMealCost());
		order.setOrderDisFee(getdeliveryCost());
		order.setOrderPreAmount(getRandom(6));
		return order;
	}
	
	
	private static int getMealCost() {
		return getRandom(3);
	}
	
	private static int getdeliveryCost() {
		return getRandom(6);
	}
	
	private static int getOrderNum() {
		return  ++num;
	}
	
	private static Item createItem(int i){
		Item item = new Item();
		item.setName(dish[i]);
		item.setPrice(prices[i]);
		item.setCount(getRandom(5) + 1);
		return item;
	}
	
	private static String createRemark() {
		return remarks[getRandom(3)];
	}
	
	private static int getRandom(int seed) {
		return (int)(Math.random() * seed);
	}
}