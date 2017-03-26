package com.qg.smpt.web.model.Json;

import java.io.IOException;

import com.qg.smpt.web.model.Order;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class OrderSerializer extends JsonSerializer<Order>{

	@Override
	public void serialize(Order value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		jgen.writeStartObject();
		jgen.writeNumberField("id", value.getId());
		jgen.writeStringField("orderStatus", value.getOrderStatus());
		jgen.writeEndObject();
	}

}
