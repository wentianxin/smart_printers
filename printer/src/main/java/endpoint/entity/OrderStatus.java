package endpoint.entity;

/**
 * Created by tisong on 4/7/17.
 */
public final class OrderStatus extends PrinterMessage {

    public int mpuId;

    public int seconds;

    public short bulkId;

    public short inNumber;

    public static OrderStatus printerMessageToOrderStatus(PrinterMessage printerMessage) {

        OrderStatus orderStatus = new OrderStatus();

        orderStatus.flag = printerMessage.flag;

        orderStatus.mpuId = printerMessage.line1;

        orderStatus.seconds = printerMessage.line2;

        orderStatus.bulkId = (short)( (printerMessage.line3 >> 16 ) & 0xFFFF) ;

        orderStatus.inNumber = (short)(printerMessage.line3 & 0xFFFF);

        orderStatus.checkSum = 0;

        return orderStatus;
    }
}
