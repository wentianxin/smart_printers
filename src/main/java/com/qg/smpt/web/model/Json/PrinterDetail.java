package com.qg.smpt.web.model.Json;

import com.qg.smpt.web.model.Printer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Created by asus2015 on 2016/11/6.
 */
@JsonSerialize(using=PrinterDetailSerializer.class)
public class PrinterDetail {


    private Printer printer;

    public PrinterDetail(Printer printer) {
        this.printer = printer;
    }

    public Printer getPrinter() {
        return printer;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }
}
