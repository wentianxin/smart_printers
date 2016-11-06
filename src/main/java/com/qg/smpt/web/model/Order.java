package com.qg.smpt.web.model;

import com.qg.smpt.printer.model.BConstants;
import com.qg.smpt.printer.model.BOrder;
import com.qg.smpt.share.ShareMem;
import com.qg.smpt.util.BytesConvert;
import com.qg.smpt.util.DebugUtil;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import com.qg.smpt.web.model.Json.OrderSerializer;

import java.io.UnsupportedEncodingException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * 订单
 */
//@JsonIgnoreProperties({"mpu","items","data", "isConvert", "orderTime", "orderRemark", "orderMealFee",
//	"orderPreAmount", "orderPayStatus", "orderDisFee","orderPreAmount","orderSum","userName", 
//	"userAddress", "userTelephone", "orderContent", "company", "expectTime"})
@JsonSerialize(using=OrderSerializer.class)
public final class Order {
    private int indexError;     // 头部错误的地方 0-图片头 1-文字头 2-二维码头

    private int textNum = 1;    // 文字要重复多少次

    private boolean hasError;   // 是否有封装错误

    private int bulkId;         // 批次id

    private int bulkIndex;      // 批次内序号

    private long sendTime;      // 订单发送时间

    private long acceptTime;     // 订单接收成功时间

    private long enterQueueTime; // 订单进入打印队列时间

    private long startPrintTime; // 订单开始打印时间

    private long printResultTime;// 订单打印结果时间;

    private long execSendTime;  //  错误订单重传时间;

    private long execAcceptTime;//  错误订单接收时间

    private long execEnterQueueTime;    // 错误订单进入打印队列时间

    private long execStartPrintTime;    // 错误订单开始打印时间

    private long execPrintResultTime;   // 错误订单打印结果时间

    public long getExecSendTime() {
        return execSendTime;
    }

    public void setExecSendTime(long execSendTime) {
        this.execSendTime = execSendTime;
    }

    public long getExecAcceptTime() {
        return execAcceptTime;
    }

    public void setExecAcceptTime(long execAcceptTime) {
        this.execAcceptTime = execAcceptTime;
    }

    public long getExecEnterQueueTime() {
        return execEnterQueueTime;
    }

    public void setExecEnterQueueTime(long execEnterQueueTime) {
        this.execEnterQueueTime = execEnterQueueTime;
    }

    public long getExecStartPrintTime() {
        return execStartPrintTime;
    }

    public void setExecStartPrintTime(long execStartPrintTime) {
        this.execStartPrintTime = execStartPrintTime;
    }

    public long getExecPrintResultTime() {
        return execPrintResultTime;
    }

    public void setExecPrintResultTime(long execPrintResultTime) {
        this.execPrintResultTime = execPrintResultTime;
    }

    public int getBulkId() {
        return bulkId;
    }

    public void setBulkId(int bulkId) {
        this.bulkId = bulkId;
    }

    public int getBulkIndex() {
        return bulkIndex;
    }

    public void setBulkIndex(int bulkIndex) {
        this.bulkIndex = bulkIndex;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public long getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(long acceptTime) {
        this.acceptTime = acceptTime;
    }

    public long getEnterQueueTime() {
        return enterQueueTime;
    }

    public void setEnterQueueTime(long enterQueueTime) {
        this.enterQueueTime = enterQueueTime;
    }

    public long getStartPrintTime() {
        return startPrintTime;
    }

    public void setStartPrintTime(long startPrintTime) {
        this.startPrintTime = startPrintTime;
    }

    public long getPrintResultTime() {
        return printResultTime;
    }

    public void setPrintResultTime(long printResultTime) {
        this.printResultTime = printResultTime;
    }

    public int getTextNum() {
        return textNum;
    }

    public void setTextNum(int textNum) {
        this.textNum = textNum;
    }

    public int getIndexError() {
        return indexError;
    }

    public void setIndexError(int indexError) {
        this.indexError = indexError;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    //　时间格式器
    private static SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");

    // 日志输出器
    private static final Logger LOGGER = Logger.getLogger(Order.class);

    private int mpu;                    //主控板id
    private List<Item> items;           //订单物品

    private byte[] data;                // 数据字节内容
    private boolean isConvert = false;  // 是否已经经过转化

    private Integer id;

    private Date orderTime;

    private String orderRemark;

    private Integer orderMealFee;

    private String orderPayStatus;

    private Integer orderDisFee;

    private Integer orderPreAmount;

    private Integer orderSum;

    private String orderStatus;

    private String userName;

    private String userAddress;

    private String userTelephone;

    private String orderContent;

    private String company;

    private String expectTime;

    private String clientName;

    private String clientAddress;

    private String clientTelephone;

    private boolean hasCompute = false;	//是否已计算总价

    private char orderType;  // 0-非加急; 1-加急

    private int userId;

    public Order() {
        this.orderType = '0';
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setOrderType(char orderType) {
        this.orderType = orderType;
    }

    public char getOrderType() {
        return orderType;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = expectTime;
    }

    public void setMpu(int mpu) {
        this.mpu = mpu;
    }

    public String getExpectTime() {
        return expectTime;
    }

    public void setConvert(boolean convert) {
        isConvert = convert;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getCompany() {
        return company;
    }

    public int getMpu() {
        return mpu;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark == null ? null : orderRemark.trim();
    }

    public Integer getOrderMealFee() {
        return orderMealFee;
    }

    public void setOrderMealFee(Integer orderMealFee) {
        this.orderMealFee = orderMealFee;
    }

    public String getOrderPayStatus() {
        return orderPayStatus;
    }

    public void setOrderPayStatus(String orderPayStatus) {
        this.orderPayStatus = orderPayStatus == null ? null : orderPayStatus.trim();
    }

    public Integer getOrderDisFee() {
        return orderDisFee;
    }

    public void setOrderDisFee(Integer orderDisFee) {
        this.orderDisFee = orderDisFee;
    }

    public Integer getOrderPreAmount() {
        return orderPreAmount;
    }

    public void setOrderPreAmount(Integer orderPreAmount) {
        this.orderPreAmount = orderPreAmount;
    }

    public Integer getOrderSum() {
        if(!hasCompute){
            orderSum = getTotalCost();
            hasCompute = true;
        }

        return orderSum;
    }

    public void setOrderSum(Integer orderSum) {
        this.orderSum = orderSum;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus == null ? null : orderStatus.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress == null ? null : userAddress.trim();
    }

    public String getUserTelephone() {
        return userTelephone;
    }

    public void setUserTelephone(String userTelephone) {
        this.userTelephone = userTelephone == null ? null : userTelephone.trim();
    }

    public String getOrderContent() {
        return orderContent;
    }

    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent == null ? null : orderContent.trim();
    }


    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientTelephone(String clientTelephone) {
        this.clientTelephone = clientTelephone;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientTelephone() {
        return clientTelephone;
    }

    public byte[] getData() {
        if(!isConvert){

        }
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    private int getTotalCost() {
        int c = 0;
        if(items != null) {
            for(Item item : items) {
                c += item.getCost();
            }
        }

        c += orderDisFee;
        c += orderMealFee;
        c -= orderPreAmount;
        c -= getOrderPreAmount();

        return c;
    }



    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n");
        buffer.append("       美团外卖       " + "\n");
        buffer.append("---------------------------\n");
        buffer.append("       " + getClientName() + "       \n");
        buffer.append("---------------------------\n");
        buffer.append("订单编号： " + getId() + "\n");
        buffer.append("下单时间： " + format.format(getOrderTime()) + "\n");
        buffer.append("预计下单时间： " + getExpectTime() + "\n");
        buffer.append("备注： " + getOrderRemark() + "\n");
        buffer.append("---------------------------\n");
        buffer.append("   菜单名     数量     小计\n");
        if(items != null) {
            for(Item item : items){
                buffer.append(item.toString() + "\n");
            }
        }
        buffer.append("---------------------------\n");
        buffer.append("               餐盒费:   " + getOrderMealFee() + "\n");
        buffer.append("               配送费:   " + getOrderDisFee() + "\n");
        buffer.append("               优惠额:   " + getOrderPreAmount() + "\n");
        buffer.append("               合 计:   " + getOrderSum() + "\n");
        buffer.append("                  " + orderPayStatus + "\n");
        buffer.append("---------------------------\n");
        buffer.append("顾客姓名: " + getUserName() + "\n");
        buffer.append("送餐地址: " + getUserAddress() + "\n");
        buffer.append("电话: " + getUserTelephone() + "\n");
        buffer.append("---------------------------\n");
        buffer.append("商家地址: " + getClientAddress() + "\n");
        buffer.append("联系方式: " + getClientTelephone() + "\n");
        return buffer.toString();

    }

    //Order对象转换为BOrder
    public BOrder orderToBOrder(short bulkId, short index){
        this.setBulkId(bulkId);
        this.setBulkIndex(index);

        BOrder bo = new BOrder();

        //设置主控板id
        bo.setId(mpu);

        //设置时间戳
        bo.setSeconds((int)(System.currentTimeMillis()));

        //设置订单序号
        bo.setOrderNumber(id);

        //设置批次,批次内序号
        bo.setBulkId(bulkId);
        bo.setInNumber(index);

        //设置校验和和填充
        bo.setCheckNum((short)0);
        bo.setPadding0((short)0);

        //设置数据域,数据长度
        byte[] data = convertOrder(hasError);
        short length = (short)data.length;
        bo.setData(data);
        bo.setLength(length);

        //设置填充
        bo.setPadding1((short)0);
        bo.size = 28 + length;
        return bo;
    }



    //将订单内容转化为字节数组
    private byte[] convertOrder(){
        // 通过userId获取用户
        User user = ShareMem.userIdMap.get(userId);

        // 获取图片的字节数据
        byte[] imageB = user != null? user.getLogoB() : null;
        // 获取图片内容的长度,以及需要补齐的字节
        int imageL = imageB != null ? imageB.length : 0;
        int fillLengthIMA = (imageL % 4) != 0 ? (4 - imageL % 4 ) : 0;

        //通过GB2312编码获取订单内容的字节数组
        byte[] orderB = new byte[0];
        try {
            orderB = this.toString().getBytes("gb2312");
        } catch (UnsupportedEncodingException e) {

        }
        //获取文本内容的长度
        int textL = orderB.length;
        //因为要字节对齐,以4字节为为单位,所以计算要填充多少位字节
        int fillLengthTEXT = (textL % 4) != 0? (4 - (textL % 4)) : 0;

        // 获取二维码的数据
        String code = user != null ? user.getUserQrcode() : "";
        byte[] codeB = (code != null && !code.equals("")) ? code.getBytes() : null;
        int codeL = codeB != null ? codeB.length : 0;
        int fillLengthCODE = (codeL % 4 != 0) ? (4 - codeL % 4) : 0;

        // 计算总数据的长度
        int size = 0;
        // 添加图片的长度
//        if(imageB != null && imageL > 0) {
//            size += (imageL + fillLengthIMA + 8);
//        }
        // 添加文字的长度
        size += (textL + fillLengthTEXT + 8);
        // 添加二维码的长度
//        if(codeB != null && codeL > 0) {
//            size += (codeL + fillLengthCODE + 8);
//        }

        //创建字节数组,大小为订单数据长度
        LOGGER.log(Level.DEBUG, "当前开始转化订单内容，总长度为[{0}]", size);
        byte[] data = new byte[size];

        int pos = 0;

//        //填充图片
//        if(imageB != null && imageL > 0) {
//            LOGGER.log(Level.DEBUG, "订单开始包装图片数据，图片长度为[{0}]", imageL);
//            // 填充图片开始字符
//            pos = BytesConvert.fillShort(BConstants.photoStart, data, pos);
//
//            // 填充图片域长度
//            pos = BytesConvert.fillShort((short) (imageL + fillLengthIMA), data, pos);
//
//            // 填充图片数据
//            pos = BytesConvert.fillByte(imageB, data, pos);
//
//            // 填充字节对齐
//            pos += fillLengthIMA;
//
//            // 填充图片内容实际长度
//            pos = BytesConvert.fillShort((short)imageL, data, pos);
//
//            // 填充图片结束字符
//            pos = BytesConvert.fillShort(BConstants.photoEnd, data, pos);
//        }


        //填充文本开始字符
        pos = BytesConvert.fillShort(BConstants.textStart,data,pos);

        //填充文本长度
        pos = BytesConvert.fillShort((short)(textL + fillLengthTEXT),data,pos);

        //填充文本数据
        pos = BytesConvert.fillByte(orderB, data, pos);

        //填充填充位
        pos  += (fillLengthTEXT + 2);

        //填充文本结束字符
        pos = BytesConvert.fillShort(BConstants.textEnd, data, pos);

//        // 填充二维码
//        if(codeB != null && codeL > 0) {
//            LOGGER.log(Level.DEBUG, "订单开始包装二维码数据，二维码为[{0}]，二维码长度为[{1}]",code, codeL);
//            // 填充二维码开始字符
//            pos = BytesConvert.fillShort(BConstants.codeStart, data, pos);
//
//            // 填充二维码长度
//            pos = BytesConvert.fillShort((short)(codeL + fillLengthCODE), data, pos);
//
//            // 填充二维码
//            pos = BytesConvert.fillByte(codeB, data, pos);
//
//            // 填充字节对齐
//            pos += fillLengthCODE;
//
//            // 填充填充位
//            pos += 2;
//
//            // 填充二维码结束字符
//            pos = BytesConvert.fillShort(BConstants.codeEnd, data, pos);
//        }
        DebugUtil.printBytes(data);
        return data;
    }

    private byte[] convertOrder(boolean hasError){


        LOGGER.debug(indexError == 0 ? "图片错误" : indexError == 1 ? "文字错误" : indexError == 2 ? "二维码错误" : "都正常" );
        short imageStart = indexError == 0 && hasError ? 0x0000 : BConstants.photoStart;
        short textStart = indexError == 1 && hasError ? 0x0000 : BConstants.textStart;
        short codeStart = indexError == 2 && hasError ? 0x0000 : BConstants.codeStart;

        // 通过userId获取用户
        User user = ShareMem.userIdMap.get(userId);

        // 获取图片的字节数据
        byte[] imageB = user != null? user.getLogoB() : null;
        // 获取图片内容的长度,以及需要补齐的字节
        int imageL = imageB != null ? imageB.length : 0;
        int fillLengthIMA = (imageL % 4) != 0 ? (4 - imageL % 4 ) : 0;

        //通过GB2312编码获取订单内容的字节数组
        byte[] orderB = new byte[0];
        try {
            orderB = this.toString().getBytes("gb2312");
        } catch (UnsupportedEncodingException e) {

        }
        //获取文本内容的长度
        int textL = orderB.length;
        //因为要字节对齐,以4字节为为单位,所以计算要填充多少位字节
        int fillLengthTEXT = (textL % 4) != 0? (4 - (textL % 4)) : 0;

        // 获取二维码的数据
        String code = user != null ? user.getUserQrcode() : "";
        byte[] codeB = (code != null && !code.equals("")) ? code.getBytes() : null;
        int codeL = codeB != null ? codeB.length : 0;
        int fillLengthCODE = (codeL % 4 != 0) ? (4 - codeL % 4) : 0;

        // 计算总数据的长度
        int size = 0;
        // 添加图片的长度
        if(imageB != null && imageL > 0) {
            size += (imageL + fillLengthIMA + 8);
        }
        // 添加文字的长度
        size += (textNum * (textL + fillLengthTEXT) + 8);
        // 添加二维码的长度
        if(codeB != null && codeL > 0) {
            size += (codeL + fillLengthCODE + 8);
        }

        //创建字节数组,大小为订单数据长度
        LOGGER.log(Level.DEBUG, "当前开始转化订单内容，总长度为[{0}]", size);
        byte[] data = new byte[size];

        int pos = 0;

        //填充图片
        if(imageB != null && imageL > 0) {
            LOGGER.log(Level.DEBUG, "订单开始包装图片数据，图片长度为[{0}]", imageL);
            // 填充图片开始字符
            pos = BytesConvert.fillShort(imageStart, data, pos);

            // 填充图片域长度
            pos = BytesConvert.fillShort((short) (imageL + fillLengthIMA), data, pos);

            // 填充图片数据
            pos = BytesConvert.fillByte(imageB, data, pos);

            // 填充字节对齐
            pos += fillLengthIMA;

            // 填充图片内容实际长度
            pos = BytesConvert.fillShort((short)imageL, data, pos);

            // 填充图片结束字符
            pos = BytesConvert.fillShort(BConstants.photoEnd, data, pos);
        }


        //填充文本开始字符
        pos = BytesConvert.fillShort(textStart,data,pos);

        //填充文本长度
        pos = BytesConvert.fillShort((short)(textNum * (textL + fillLengthTEXT)),data,pos);

        //填充文本数据
        for(int i = 0; i < textNum; i++) {
            pos = BytesConvert.fillByte(orderB, data, pos);
            pos += fillLengthTEXT;
        }

        //填充填充位
        pos  +=  2;

        //填充文本结束字符
        pos = BytesConvert.fillShort(BConstants.textEnd, data, pos);

        // 填充二维码
        if(codeB != null && codeL > 0) {
            LOGGER.log(Level.DEBUG, "订单开始包装二维码数据，二维码为[{0}]，二维码长度为[{1}]",code, codeL);
            // 填充二维码开始字符
            pos = BytesConvert.fillShort(codeStart, data, pos);

            // 填充二维码长度
            pos = BytesConvert.fillShort((short)(codeL + fillLengthCODE), data, pos);

            // 填充二维码
            pos = BytesConvert.fillByte(codeB, data, pos);

            // 填充字节对齐
            pos += fillLengthCODE;

            // 填充填充位
            pos += 2;

            // 填充二维码结束字符
            pos = BytesConvert.fillShort(BConstants.codeEnd, data, pos);
        }
        DebugUtil.printBytes(data);
        return data;
    }

}