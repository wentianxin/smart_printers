package com.qg.smpt.web.model;

import com.qg.smpt.printer.OrderToPrinter;
import com.qg.smpt.util.ImageUtil;

import java.io.File;
import java.util.Deque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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


    private byte[] logoB;


    /**
     * 接下来的批次ID
     */
    private AtomicInteger currentBulkOrderId;

    private AtomicReference<OrderToPrinter> orderToPrinter;

    private Thread orderToPrinterThread;

    private Deque<BulkOrder> nonSendBulkOrder; // 待发送的批次队列

    private AtomicReference<BulkOrder> packingBulkOrder; // 正在组装的批次队列

    private Queue<Printer> printers = null;

    public User() {
        currentBulkOrderId = new AtomicInteger();
        nonSendBulkOrder = new ConcurrentLinkedDeque<>();
        packingBulkOrder = new AtomicReference<>(new BulkOrder());
        printers = new ConcurrentLinkedDeque<>();
        orderToPrinter = new AtomicReference<>(new OrderToPrinter(this));
    }

    public void setOrderToPrinterThread(Thread orderToPrinterThread) {
        this.orderToPrinterThread = orderToPrinterThread;
    }

    public void setCurrentBulkOrderId(AtomicInteger currentBulkOrderId) {
        this.currentBulkOrderId = currentBulkOrderId;
    }

    public int getCurrentBulkOrderId() {
        return currentBulkOrderId.incrementAndGet();
    }

    public Thread getOrderToPrinterThread() {
        return orderToPrinterThread;
    }

    private boolean inited; // user 的声明周期, 是否已经初始化

    public void setInited(boolean inited) {
        this.inited = inited;
    }

    public boolean isInited() {
        return inited;
    }

    public void setNonSendBulkOrder(Deque<BulkOrder> nonSendBulkOrder) {
        this.nonSendBulkOrder = nonSendBulkOrder;
    }

    public Deque<BulkOrder> getNonSendBulkOrder() {
        return nonSendBulkOrder;
    }

    public void setPackingBulkOrder(AtomicReference<BulkOrder> packingBulkOrder) {
        this.packingBulkOrder = packingBulkOrder;
    }

    public AtomicReference<BulkOrder> getPackingBulkOrder() {
        return packingBulkOrder;
    }

    public void setOrderToPrinter(AtomicReference<OrderToPrinter> orderToPrinter) {
        this.orderToPrinter = orderToPrinter;
    }


    public AtomicReference<OrderToPrinter>getOrderToPrinter() {
        return orderToPrinter;
    }

    public boolean isConvert() {
        return isConvert;
    }

    public void setConvert(boolean convert) {
        isConvert = convert;
    }

    private boolean isConvert = false;

    public void setPrinters(Queue<Printer> printers) {
        this.printers = printers;
    }

    public Queue<Printer> getPrinters() {
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