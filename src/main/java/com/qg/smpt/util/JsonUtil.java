package com.qg.smpt.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;  
import org.codehaus.jackson.map.SerializationConfig;  
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;  
import org.codehaus.jackson.type.TypeReference;

import com.qg.smpt.printer.PrinterConnector;


/**
 * Json格式转化工具
 * @author asus2015
 *
 */
public class JsonUtil {
	private static final Logger LOGGER = Logger.getLogger(JsonUtil.class);
	private static ObjectMapper mapper = null;
	
	static {
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
	
	
	/**
	 * 改方法用于给json
	 * @return
	 */
	public static String jsonToMap(Object[] key, Object[] json) {
		
		
		Map map = new HashMap<>();
		int length = Math.min(key.length, json.length);
		for(int i = 0; i < length; i++){
//			LOGGER.log(Level.DEBUG,  "JsonUtil正在封装map信息,key为[{0}], value为 [{1}]", key[i], json[i]);
			map.put(key[i], json[i]);
		}
		
		String data =  objectToJson(map);
		
		LOGGER.log(Level.DEBUG, "转化后的json数据为 [{0}]", data);
		
		return data;
	}
	
	public static String objectToJson(Object object) {
		LOGGER.log(Level.DEBUG,  "JsonUtil正在进行对象转化为JSON操作,转化的对象为[{0}]", object.getClass());
		try {
			
			String json = mapper.writeValueAsString(object);
			return json;
		
		
		} catch (JsonGenerationException e) {
			LOGGER.log(Level.ERROR, "Jsonutil调用objectToJson方法出现异常", e);
			e.printStackTrace();
		} catch (JsonMappingException e) {
			LOGGER.log(Level.ERROR, "Jsonutil调用objectToJson方法出现异常", e);
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.log(Level.ERROR, "Jsonutil调用objectToJson方法出现异常", e);
			e.printStackTrace();
		}
		
		return "";
		
	}
	
	public static Object jsonToObject(String json, Class clazz) {
		LOGGER.log(Level.DEBUG,  "JsonUtil正在进行JSON转化对象操作,json数据为[{0}], 要转化的对象为[{1}]",json, clazz.toString());
			
			try {
				
				Object object = mapper.readValue(json, clazz);
				return object;
				
			} catch (JsonParseException e) {
				LOGGER.log(Level.ERROR, "Jsonutil调用jsonToObject方法出现异常", e);
				e.printStackTrace();
			} catch (JsonMappingException e) {
				LOGGER.log(Level.ERROR, "Jsonutil调用jsonToObject方法出现异常", e);
				e.printStackTrace();
			} catch (IOException e) {
				LOGGER.log(Level.ERROR, "Jsonutil调用jsonToObject方法出现异常", e);
				e.printStackTrace();
			}
			
			return null;
	}

	public static Object jsonToObject(String json, TypeReference clazz) {
		LOGGER.log(Level.DEBUG,  "JsonUtil正在进行JSON转化对象操作,json数据为[{0}], 要转化的对象为[{1}]",json, clazz.toString());

		try {

			Object object = mapper.readValue(json, clazz);
			return object;

		} catch (JsonParseException e) {
			LOGGER.log(Level.ERROR, "Jsonutil调用jsonToObject方法出现异常", e);
			e.printStackTrace();
		} catch (JsonMappingException e) {
			LOGGER.log(Level.ERROR, "Jsonutil调用jsonToObject方法出现异常", e);
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.log(Level.ERROR, "Jsonutil调用jsonToObject方法出现异常", e);
			e.printStackTrace();
		}

		return null;
	}
}
