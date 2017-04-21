package com.qg.smpt.repository.handler;

import com.qg.smpt.model.User;

/**
 * Created by asus2015 on 2017/4/9.
 */
public interface UserHandler {
    public abstract User login(String account, String password);

    public abstract boolean register(User user);

    public abstract User getUser(int userId);

    public abstract boolean logout(User user);

    public abstract boolean update(User user);
}
