package com.qg.smpt.repository.handler;

import com.qg.smpt.model.Printer;

import java.util.List;

/**
 * Created by asus2015 on 2017/4/9.
 */
public interface PrinterHandler {
    public abstract List<Printer> getPrinters(int userId);
}

