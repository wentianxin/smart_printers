package com.qg.smpt.service;

import com.qg.smpt.model.User;
import com.qg.smpt.util.AbstractPrinterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by asus2015 on 2017/4/9.
 */
@Service
public class UserService {

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
