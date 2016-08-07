package com.qg.smpt.web.model;

import com.qg.smpt.util.ImageUtil;

import java.io.File;
import java.util.List;

public class User {

    private Integer id;

    private String userName;

    private String userAccount;

    private String userPassword;

    private Integer userPrinters;

    private String userLogo;

    private String userQrcode;

    private String userStore;

    private String userAddress;

    private String userPhone;

    private List<Printer> printers;

    private byte[] logoB;

    public boolean isConvert() {
        return isConvert;
    }

    public void setConvert(boolean convert) {
        isConvert = convert;
    }

    private boolean isConvert = false;

    public void setPrinters(List<Printer> printers) {
        this.printers = printers;
    }

    public List<Printer> getPrinters() {
        return printers;
    }

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
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount == null ? null : userAccount.trim();
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword == null ? null : userPassword.trim();
    }

    public Integer getUserPrinters() {
        return userPrinters;
    }

    public void setUserPrinters(Integer userPrinters) {
        this.userPrinters = userPrinters;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo == null ? null : userLogo.trim();
    }

    public String getUserQrcode() {
        return userQrcode;
    }

    public void setUserQrcode(String userQrcode) {
        this.userQrcode = userQrcode == null ? null : userQrcode.trim();
    }

    public String getUserStore() {
        return userStore;
    }

    public void setUserStore(String userStore) {
        this.userStore = userStore == null ? null : userStore.trim();
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress == null ? null : userAddress.trim();
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone == null ? null : userPhone.trim();
    }

    public byte[] getLogoB() {
        if(!isConvert) {
            logoB = loadLogo();
            isConvert = true;
        }

        return logoB;
    }

    private byte[] loadLogo() {
        String path = this.getClass().getResource("/").getPath();

        path = path.substring(0, path.indexOf("/classes"));

        path += (File.separator + userLogo);

        byte[] data = ImageUtil.getImage(path);

        return data;
    }



}