package com.qg.smpt.lee.processor;

import com.qg.smpt.lee.model.User;
import com.qg.smpt.lee.service.UserService;
import com.qg.smpt.lee.util.Constant;
import com.qg.smpt.share.ShareMem;
import com.qg.smpt.util.JsonUtil;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by asus2015 on 2017/4/8.
 */
@Controller
public class UserProcessor {

    @Autowired
    private UserService userService;

    private static Logger LOGGER = Logger.getLogger(UserProcessor.class);

    @RequestMapping(value="/login", method= RequestMethod.POST, produces="application/json;charset=utf-8" )
    @ResponseBody
    public String login(String data) {
        // 根据输入组装成用户对象
        User user = (User) JsonUtil.jsonToObject(data, User.class);

        int retcode = Constant.FALSE;

        // 检查用户输入是否合法，不合法则返回错误
        if(!checkCorrect(user)){
            return JsonUtil.jsonToMap(new String[]{"retcode"}, new String[]{String.valueOf(retcode)});
        }

        // 执行登陆方法用户
        User loginUser = userService.login(user);

        // 设置登录结果状态
        retcode = (loginUser != null ? Constant.TRUE : Constant.FALSE);

        // 成功登陆，则返回带有用户 id 的成功信息
        if(retcode == Constant.TRUE) {
            LOGGER.log(Level.INFO,"用户 [{}] 成功登陆系统，他的编号为 [{}]",loginUser.getUserName(), loginUser.getId());
            return JsonUtil.jsonToMap(new String[]{"retcode","userId"}, new Object[]{retcode, loginUser.getId().toString()});
        }

        return JsonUtil.jsonToMap(new String[]{"retcode"}, new Object[]{retcode});
    }



    private boolean checkCorrect(User user) {
        return true;
    }

    @RequestMapping(value="/register", method= RequestMethod.POST, produces="application/json;charset=utf-8" )
    @ResponseBody
    public String register(String data) {
        User newUser = (User)JsonUtil.jsonToObject(data, User.class);

        // 检查用户信息是否正确,正确则执行注册用户方法,错误则返回错误状态
        try{
            int retcode = (checkUserInfo(newUser) ? userService.register(newUser) ? Constant.TRUE : Constant.FALSE : Constant.FALSE);

            return JsonUtil.jsonToMap(new String[]{"retcode"}, new Object[]{retcode});

        }catch(Exception e){
            return JsonUtil.jsonToMap(new String[]{"retcode"}, new Object[]{Constant.FALSE});
        }
    }

    private boolean checkUserInfo(User user) {
            user.setUserLogo("");
            user.setUserQrcode("");
            return true;

    }

    public String logout() {
        return "";
    }

    public String updateBase() {
        return "";
    }

}
