//package com.qg.smpt.lwc;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.nio.channels.SocketChannel;
//import java.util.ArrayDeque;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Queue;
//
//import org.apache.ibatis.javassist.expr.NewArray;
//
//import com.qg.smpt.share.ShareMem;
//import com.qg.smpt.web.model.Item;
//import com.qg.smpt.web.model.Order;
//import com.qg.smpt.web.model.Printer;
//
//
//
//public class Server {
//	private static int port = 30000;
//	private static ServerSocket serverSocket;
//	public static Printer printer = new Printer(1);
//	
//	
//	public static void main(String[] args) throws Exception {
//		serverSocket = new ServerSocket(port);
//		serverSocket.setSoTimeout(600000);
//		InitPrinter();
//		initOrder();
//		
//		while(true) {
//			Socket socket = serverSocket.accept();
////			ShareMem.printerSocket.put(printer, socket);
//			System.out.println("有客户端接入了");
//			HandleClient client = new HandleClient(socket);
//			client.start();
//		}
//		
//	}
//	
//	
//	
//	private static void InitPrinter() {
//		ShareMem.priBufferQueueMap = new HashMap<Printer, Queue<Order>>();
//		
//		ShareMem.priExceQueueMap = new HashMap<Printer, Queue<Order>>();
//		
//		ShareMem.priLinkSocketMap = new HashMap<Printer, SocketChannel>();
//		
//		ShareMem.printerIdMap = new HashMap<>();
//		
//		ShareMem.priSentQueueMap = new HashMap<>();
//		
//		ShareMem.userIdMap = new HashMap<>();
//		
//		ShareMem.userListMap = new HashMap<>();
//		
////		ShareMem.printerSocket = new HashMap<>();
//		
//		ShareMem.printerIdMap.put(1, printer);
//		
//		ShareMem.priBufferQueueMap.put(printer, new ArrayDeque<>());
//		
//		ShareMem.priSentQueueMap.put(printer, new ArrayDeque<>());
//		
//		
//		
//		
//	}
//	
//	
//	
//	
//	private static void initOrder() throws UnsupportedEncodingException {
//		OrderBuilder builder = new OrderBuilder();
//		Queue<Order> orders = ShareMem.priBufferQueueMap.get(printer);
//		for(int i = 0; i < 1000; i++) {
//			Order o = builder.produceOrder();
//			orders.add(o);
//		}
//		
//		System.out.println("生成订单完毕");
//	}
//}
//
//
////订单生成器
//class OrderBuilder {
//	private int num = 0;
//	
//	//公司名称
//	private static String[] companys = {"美团外卖", "饿了吗", "百度淘米", "百度外卖"};
//	
//	//商家信息
//	private static String shops[] = {"麦当劳", "肯德基", "好难吃的地方", "想不出什么地方了"};
//	private static String address[] = {"gogo新天地一楼25号","广大商业区", "广东工业大学饭堂", "地址未知" };
//	private static String contact[] = {"85241523", "84523651", "15521232546", "15622365842"};
//	
//	//菜单
//	private static String dish[] = {"西红柿炒番茄", "葡萄炒木耳", "西瓜炒香蕉", "榴莲鸡蛋", "童子鸡"};
//	private static int prices[] = {4, 4, 5, 3, 8};
//	
//	//顾客信息
//	private static String customers[] = {"陈俊铭", "温天信", "张诗婷", "李伟淙", "许艺茂", "方锐"};
//	private static String cAddress[] = {"广工西三736", "广工西四612", "广工东十三xxx", "广工西三741", "广工西三747", "广工西三2xx"};
//	private static String cContact[] = {"15521256251", "18852423652", "13432252452", "15622365455", "18819255400", "15695542562"};
//	
//	private static String remarks[] = {"加饭", "晚点来", "加菜"};
//	
//	private static String expectTimes[] = {"10:30", "11.30", "12.30", "5.30", "6.30", "7.30"};
//	
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
//	
//	private  int getMealCost() {
//		return getRandom(3);
//	}
//	
//	private  int getdeliveryCost() {
//		return getRandom(6);
//	}
//	
//	private  String getOrderNum() {
//		return "订单" + (++num);
//	}
//	
//	private Item createItem(int i){
//		Item item = new Item();
//		item.setName(dish[i]);
//		item.setPrice(prices[i]);
//		item.setCount(getRandom(5) + 1);
//		return item;
//	}
//	
//	private  String createRemark() {
//		return remarks[getRandom(3)];
//	}
//	
//	private  int getRandom(int seed) {
//		return (int)(Math.random() * seed);
//	}
//}
