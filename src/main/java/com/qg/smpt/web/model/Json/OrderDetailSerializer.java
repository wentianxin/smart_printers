package com.qg.smpt.web.model.Json;

import com.qg.smpt.util.TimeUtil;
import com.qg.smpt.web.model.Order;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

/**
 * Created by asus2015 on 2016/11/6.
 */
public class OrderDetailSerializer extends JsonSerializer<OrderDetail> {
    @Override
    public void serialize(OrderDetail value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        Order order = value.getOrder();
        jgen.writeNumberField("id", order.getId());
        jgen.writeStringField("orderStatus", order.getOrderStatus());
        jgen.writeNumberField("bulkId", order.getBulkId());
        jgen.writeNumberField("bulkIndex", order.getBulkIndex());
        jgen.writeStringField("sendTime", TimeUtil.timeToString(order.getSendTime()));
        jgen.writeStringField("acceptTime", TimeUtil.timeToString(order.getAcceptTime()));
        jgen.writeStringField("enterQueueTime", TimeUtil.timeToString(order.getEnterQueueTime()));
        jgen.writeStringField("startPrintTime", TimeUtil.timeToString(order.getPrintResultTime()));
        jgen.writeStringField("execSendTime", TimeUtil.timeToString(order.getExecSendTime()));
        jgen.writeStringField("execAcceptTime", TimeUtil.timeToString(order.getExecAcceptTime()));
        jgen.writeStringField("execEnterQueueTime", TimeUtil.timeToString(order.getExecEnterQueueTime()));
        jgen.writeStringField("execStartPrintTime", TimeUtil.timeToString(order.getExecStartPrintTime()));
        jgen.writeStringField("execPrintResultTime", TimeUtil.timeToString(order.getExecPrintResultTime()));
        jgen.writeEndObject();
    }
}
