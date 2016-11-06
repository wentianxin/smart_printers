package com.qg.smpt.web.model.Json;

import com.qg.smpt.web.model.Printer;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

/**
 * Created by asus2015 on 2016/11/6.
 */
public class PrinterDetailSerializer extends JsonSerializer<PrinterDetail> {
    @Override
    public void serialize(PrinterDetail value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        Printer printer = value.getPrinter();
        jgen.writeStartObject();
        jgen.writeNumberField("id", printer.getId());
        jgen.writeStringField("printerStatus", printer.getPrinterStatus());
        jgen.writeNumberField("sendedOrderNum", printer.getSendedOrdersNum());
        jgen.writeNumberField("unsendedOrderNum", printer.getUnsendedOrdersNum());
        jgen.writeNumberField("printSuccessNum", printer.getPrintSuccessNum());
        jgen.writeNumberField("printErrorNum", printer.getPrintErrorNum());
        jgen.writeNumberField("successRate", printer.getSuccessRate());
        jgen.writeEndObject();
    }
}
