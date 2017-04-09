package eneity.state;

import endpoint.entity.PrinterMessage;
import endpoint.entity.PrinterStatus;
import eneity.exception.PrinterStatusException;
import model.Printer;

/**
 * Created by tisong on 4/7/17.
 */
public class PrinterStateMachine {

    public void fireStateMachine(Printer printer, Object msg, PrinterEvent event) throws PrinterStatusException {
        PrinterState printerState = printer.getPrinterState();
        switch (event) {
            case Connect:     printerState.connect((PrinterMessage) msg);break;
            case SetMemroyOk: printerState.receiveMemoryOk(); break;
            case SendOrder:   printerState.sendOrders(); break;
            case UpdateHealth:printerState.updatePrinterHealth();break;
            case Close:       printerState.close(); break;
        }

    }




    /**
     * 打印机对应的事件(每个事件对应一个方法)
     */
    public static enum PrinterEvent {
        Connect, SetMemroyOk, SendOrder, UpdateHealth, Close
    }


    public static interface PrinterState {
        void connect(PrinterMessage msg) throws PrinterStatusException;
        void receiveMemoryOk() throws PrinterStatusException;
        void sendOrders() throws PrinterStatusException;
        void updatePrinterHealth() throws PrinterStatusException;
        void close() throws PrinterStatusException;
    }

    /**
     * PrinterState 实现抽象类, 实现所有方法, 并抛出该状态无法执行该动作异常
     */
    static abstract class AbstarctPrinterStatus implements PrinterState {

        protected Printer printer;

        public void setPrinter(Printer printer) {
            this.printer = printer;
        }

        @Override
        public void connect(PrinterMessage msg) throws PrinterStatusException {
            throw new PrinterStatusException("打印机当前状态无法执行 connect 事件; 打印机状态: " + printer.getPrinterState());
        }

        @Override
        public void receiveMemoryOk() throws PrinterStatusException {
            throw new PrinterStatusException("打印机当前状态无法执行  setMemoryOk 事件; 打印机状态: " + printer.getPrinterState());
        }

        @Override
        public void sendOrders() throws PrinterStatusException {
            throw new PrinterStatusException("打印机当前状态无法执行 sendOrder 事件; 打印机状态: " + printer.getPrinterState());
        }

        @Override
        public void updatePrinterHealth() throws PrinterStatusException {
            throw new PrinterStatusException("打印机当前状态无法执行 updateHealth 事件; 打印机状态: " + printer.getPrinterState());
        }

        @Override
        public void close() throws PrinterStatusException {
            throw new PrinterStatusException("打印机当前状态无法执行 closed 事件; 打印机状态: " + printer.getPrinterState());
        }
    }

    /**
     * 已成功接入后的打印机状态
     */
    static abstract class AbstractHasConnectedPrinterStatus extends AbstarctPrinterStatus {


        @Override
        public void updatePrinterHealth() {

        }

        @Override
        public void close() {

        }
    }

    public static class PrinterNonConnectionState extends AbstarctPrinterStatus {
        @Override
        public void connect(PrinterMessage msg) {


            printer.setPrinterState(printer.getPrinterConnectionState());
        }
    }

    public static class PrinterConnectionState extends AbstarctPrinterStatus{

        @Override
        public void connect(PrinterMessage printerStatus) {
            // 获取打印机对象
         //   int printerId = printerStatus.mpuId;

            // 获取商家对象

            //

            printer.setPrinterState(printer.getPrinterMemoryNotOkState());
        }

        @Override
        public String toString() {
            return PrinterStateEnum.Connected.toString();
        }
    }



    public static class PrinterMemoryOkState extends AbstractHasConnectedPrinterStatus{

        @Override
        public void sendOrders() {

        }

        @Override
        public String toString() {
            return PrinterStateEnum.MemoryOk.toString();
        }
    }

    public static class PrinterMemoryNotOkState extends AbstractHasConnectedPrinterStatus{

        @Override
        public void receiveMemoryOk() {

        }


        @Override
        public String toString() {
            return PrinterStateEnum.MemoryNotOk.toString();
        }
    }

    public static class PrinterClosedState extends AbstarctPrinterStatus{

        @Override
        public void connect(PrinterMessage msg) {

        }

        @Override
        public String toString() {
            return PrinterStateEnum.Closed.toString();
        }
    }
}
