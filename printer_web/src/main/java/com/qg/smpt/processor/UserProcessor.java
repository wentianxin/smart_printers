package com.qg.smpt.processor;

import com.qg.smpt.eneity.ReturnJSON;
import com.qg.smpt.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by tisong on 4/8/17.
 */
@Controller
public class UserProcessor {


    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public ReturnJSON loginAccount(@RequestBody User user) {
        return null;
    }


}
