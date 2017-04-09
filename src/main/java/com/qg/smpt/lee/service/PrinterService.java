package com.qg.smpt.lee.service;

import com.qg.smpt.lee.model.Printer;
import com.qg.smpt.lee.util.AbstractPrinterUtil;

import java.util.List;

/**
 * Created by asus2015 on 2017/4/8.
 */
public class PrinterService {
    private AbstractPrinterUtil abstractPrinterUtil;

    public List<Printer> getPrinters(int userId) {
        return abstractPrinterUtil.getPrinters(userId);
    }
}
