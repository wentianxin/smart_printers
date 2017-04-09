package endpoint;

import endpoint.entity.Constants;
import endpoint.entity.PrinterMessage;
import model.Printer;
import eneity.state.PrinterStateMachine;
import eneity.state.PrinterStateMachine.*;
/**
 * Created by tisong on 4/7/17.
 */
public class PrinterMessageProcessor implements Runnable{


    private PrinterMessage msg;

    private PrinterStateMachine printerStateMachine;

    public PrinterMessageProcessor(PrinterMessage msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        // 订单状态, 打印机状态(OK)
        byte flag = (byte) ((msg.flag >> 8) & 0xff);
        switch (flag) {
            case Constants.connectStatus : processPrinterConnectStatus(); break;
            case Constants.okStatus   : processPrinterSetMemoryOk();break;
            case Constants.printHealth: processPrinterHealth(); break;
            case Constants.orderStatus: processOrderStatus();   break;
            case Constants.bulkStatus : processBulkStatus();    break;
            default:
        }
    }



    public void reset(PrinterMessage msg) {
        this.msg = msg;
    }



    private void processPrinterConnectStatus() {
        // 不做任何处理
    }

    private void processPrinterHealth() {
        // 获取打印机对象,
        Printer printer = null;
        printerStateMachine.fireStateMachine(printer, msg, PrinterEvent.UpdateHealth);
    }

    private void processPrinterSetMemoryOk() {
        Printer printer = null;

        printerStateMachine.fireStateMachine(printer, msg, PrinterEvent.SetMemroyOk);
    }



    private void processOrderStatus() {


    }

    private void processBulkStatus() {

    }

}
