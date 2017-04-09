package com.qg.smpt.lee.service;

import com.qg.smpt.lee.model.User;
import com.qg.smpt.lee.util.AbstractPrinterUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by asus2015 on 2017/4/8.
 */
public class UserService {
    @Autowired
    private AbstractPrinterUtil abstractPrinterUtil;

    public User login(User user) {
        return abstractPrinterUtil.login(user.getUserAccount(), user.getUserPassword());
    }

    public boolean register(User user) {
        return abstractPrinterUtil.register(user);
    }

    public User getUser(int userId) {
        return abstractPrinterUtil.getUser(userId);
    }

    public boolean logout(User user) {
        return abstractPrinterUtil.logout(user);
    }

    public boolean updateBase(User user) {
        return abstractPrinterUtil.update(user);
    }
}
