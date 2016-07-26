package com.qg.smpt.web.processor;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qg.smpt.share.ShareMem;
import com.qg.smpt.util.JsonUtil;
import com.qg.smpt.web.model.Printer;

public class PrinterController {
	
	@RequestMapping(value="/printer", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String seePrinterStatus(int userId) {
		// 根据用户id获取打印机
		List<Printer> printers = ShareMem.userListMap.get(userId);
		
		return JsonUtil.objectToJson(printers);
	}
}
