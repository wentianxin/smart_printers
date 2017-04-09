package com.qg.smpt.repository.redis;

import com.qg.smpt.model.Printer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 散列方式存储
 *  key = printer:userId
 *      value = printerId : printerStatus
 *      value = printerId : printerStatus
 */
@Repository
public class PrinterRedis {

    @Autowired
    private Jedis jedis;

    private static final String KEY = "printer:";


    public void insertPrintersByUserId(Integer userId, List<Printer> printers) {
        if (userId == null) {
            return ;
        }
        jedis.hmset(KEY + userId, printersToMap(printers));
    }


    public void updatePrinterStatus(Integer userId, Integer printerId, String printerStatus) {
        if (userId == null || printerId == null) {
            return ;
        }
        Map<String, String> map = new HashMap<>();
        map.put(printerId.toString(), printerStatus);
        jedis.hmset(KEY + userId, map);
    }

    private Map<String, String> printersToMap(List<Printer> printers) {
        final Map<String, String> map = new HashMap<>();
        for (Printer p : printers) {
            map.put(p.getId().toString(), p.getPrinterStatus());
        }
        return map;
    }



}
