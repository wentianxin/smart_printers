package com.qg.smpt.service;

import com.qg.smpt.model.Printer;
import com.qg.smpt.util.AbstractPrinterUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by asus2015 on 2017/4/9.
 */
@Service
public class PrinterService {
    private AbstractPrinterUtil abstractPrinterUtil;

    public List<Printer> getPrinters(int userId) {
        return abstractPrinterUtil.getPrinters(userId);
    }
}
