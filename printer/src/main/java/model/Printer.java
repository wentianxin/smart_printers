package model;

import eneity.state.PrinterStateEnum;
import io.netty.channel.ChannelHandlerContext;
import eneity.state.PrinterStateMachine.*;

import java.util.List;

/**
 * Created by tisong on 4/7/17.
 */
public class Printer {

    private User user;

    private ChannelHandlerContext channel;


    private volatile PrinterState printerState;

    private PrinterConnectionState  printerConnectionState;
    private PrinterMemoryNotOkState printerMemoryNotOkState;
    private PrinterMemoryOkState    printerMemoryOkState;
    private PrinterClosedState      printerClosedState;

    private List<BulkOrder> sendedBulkOrder;


    private PrinterStateEnum printerStateEnum;

    public List<BulkOrder> getSendedBulkOrder() {
        return sendedBulkOrder;
    }

    public void setSendedBulkOrder(List<BulkOrder> sendedBulkOrder) {
        this.sendedBulkOrder = sendedBulkOrder;
    }

    public PrinterStateEnum getPrinterStateEnum() {
        return printerStateEnum;
    }

    public void setPrinterStateEnum(PrinterStateEnum printerStateEnum) {
        this.printerStateEnum = printerStateEnum;
    }

    public PrinterState getPrinterState() {
        return printerState;
    }
    public void setPrinterState(PrinterState printerState) {
        this.printerState = printerState;
    }


    public User getUser() {
        return user;
    }

    public PrinterConnectionState getPrinterConnectionState() {
        return printerConnectionState;
    }

    public PrinterMemoryNotOkState getPrinterMemoryNotOkState() {
        return printerMemoryNotOkState;
    }

    public PrinterMemoryOkState getPrinterMemoryOkState() {
        return printerMemoryOkState;
    }

    public PrinterClosedState getPrinterClosedState() {
        return printerClosedState;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ChannelHandlerContext getChannel() {
        return channel;
    }

    public void setChannel(ChannelHandlerContext channel) {
        this.channel = channel;
    }
}

