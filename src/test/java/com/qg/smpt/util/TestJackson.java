package com.qg.smpt.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.qg.smpt.web.model.OrderRequest;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.SerializationConfig; 
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.model.Printer;
import com.qg.smpt.web.model.User;


public class TestJackson {
	private JsonGenerator jsonGenerator = null;
    private ObjectMapper mapper = null;
	
	
	@Before
	public void setUp() throws Exception {
		mapper = new ObjectMapper();  
        //设置将对象转换成JSON字符串时候:包含的属性不能为空或"";    
        //Include.Include.ALWAYS 默认    
        //Include.NON_DEFAULT 属性为默认值不序列化    
        //Include.NON_EMPTY 属性为 空（""）  或者为 NULL 都不序列化    
        //Include.NON_NULL 属性为NULL 不序列化    
        mapper.setSerializationInclusion(Inclusion.NON_EMPTY);  
        //设置将MAP转换为JSON时候只转换值不等于NULL的  
        mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);  
        mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);  
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));  
        //设置有属性不能映射成PO时不报错  
        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);  
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Ignore
	@Test
	public void testCommonObject() throws JsonGenerationException, JsonMappingException, IOException {
		Order o = new Order();
		o.setOrderStatus("110");
		o.setId(1);
		
		String json = mapper.writeValueAsString(o);
		System.out.println(json);
	}
	
	@Ignore
	@Test
	public void testList() throws JsonGenerationException, JsonMappingException, IOException {
		List<Order> orders = new ArrayList<Order>();
		
		for(int i = 0; i < 10; i++){
			Order o = new Order();
			o.setOrderStatus("110");
			
			o.setId(i);
			orders.add(o);
		}
		
		
		
		String json = mapper.writeValueAsString(orders);
		System.out.println(json);
	}
	
	@Ignore
	@Test
	public void convert() throws JsonParseException, JsonMappingException, IOException {
		String name="{\"orderStatus\":\"110\"}";
		Order order = (Order)JsonUtil.jsonToObject(name, Order.class);
		System.out.println(order.getOrderStatus());
	}


	@Test
	public void testBoolean() {
		List<OrderRequest> a = (List<OrderRequest>)JsonUtil.jsonToObject("[{\"number\":5,\"size\":1,\"orderType\":0}]", new TypeReference<List<OrderRequest>>(){});
		System.out.println(a.get(0).getNumber());
	}
}
