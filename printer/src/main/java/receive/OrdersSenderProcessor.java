package receive;

import eneity.exception.PrinterStatusException;
import model.*;
import model.User.SendOrderContext;
import eneity.state.PrinterStateMachine;
import util.factory.PrinterStateMachineSingleton;

import java.util.Queue;

/**
 * Created by tisong on 4/7/17.
 */
public class OrdersSenderProcessor implements Runnable {

    private User user;

    private final int SEND_INTERVAL = 1000;

    public OrdersSenderProcessor(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        check();

        Printer printer = selectPrinter();

        send(printer);
    }

    private void check() {
        SendOrderContext context = user.getSendOrderContext();
        Queue<Printer> printerQueue = context.getCanUsePrinters();

        if (printerQueue.size() >= 1 && context.getNonSendBulkOrder().size() >= 1) {
            return ;
        }

        try {
            wait(SEND_INTERVAL);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BulkOrder bulkOrder = context.getPackingBulkOrder().get();
        context.getPackingBulkOrder().set(new BulkOrder());
        context.getNonSendBulkOrder().add(bulkOrder);
    }

    private Printer selectPrinter() {
        return user.getSendOrderContext().getCanUsePrinters().remove();
    }


    private void send(Printer printer) {

        BulkOrder bulkOrder = user.getSendOrderContext().getNonSendBulkOrder().poll();
//        for (Order order: bulkOrder.getOrders()) {
//            order.setMpu(printer.getId());
//        }


        try {
            PrinterStateMachineSingleton.getPrinterStateMachine().fireStateMachine(printer, null, PrinterStateMachine.PrinterEvent.SendOrder);
        } catch (PrinterStatusException e) {
            e.printStackTrace();
        }
    }

    private void reset(User user) {
        this.user = user;
    }

}
