package com.qg.smpt.processor;

import com.qg.smpt.model.Printer;
import com.qg.smpt.service.PrinterService;
import com.qg.smpt.util.JsonUtil;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by tisong on 4/8/17.
 */
@Controller
@RequestMapping("/user/{userId}")
public class PrinterProcessor {
    private static final Logger LOGGER = Logger.getLogger(PrinterProcessor.class);

    @Autowired
    private PrinterService printerService;


    @RequestMapping(value="/printer", method= RequestMethod.GET, produces="application/json;charset=utf-8" )
    @ResponseBody
    public String getPrinters(@PathVariable int userId) {
        LOGGER.log(Level.DEBUG, "查看用户[{0}]的打印机状态 ", userId);

        // 获取用户的打印机
        List<Printer> printers = printerService.getPrinters(userId);

        String json = JsonUtil.jsonToMap(new String[]{"retcode","data"},
                new Object[]{1 ,printers});

        LOGGER.log(Level.DEBUG, "当前转化的信息为 [{0}]", json);

        return json;
    }

}
