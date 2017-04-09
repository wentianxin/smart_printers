package com.qg.smpt.lee.model;

import java.util.List;

/**
 * Created by asus2015 on 2017/4/8.
 */
public class User {
    private Integer id;         // 用户编号

    private String userName;    // 用户名

    private String userAccount; // 用户账号

    private String userPassword;// 用户密码

    private String userLogo;    // 用户 logo

    private String userQrcode;  // 用户二维码

    private String userStore;   // 用户店铺名字

    private String userAddress; // 用户地址

    private String userPhone;   // 用户电话

    private List<Printer> printers; // 用户的打印机集合

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public String getUserQrcode() {
        return userQrcode;
    }

    public void setUserQrcode(String userQrcode) {
        this.userQrcode = userQrcode;
    }

    public String getUserStore() {
        return userStore;
    }

    public void setUserStore(String userStore) {
        this.userStore = userStore;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
