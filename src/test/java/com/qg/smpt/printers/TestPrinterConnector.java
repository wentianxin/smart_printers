package com.qg.smpt.printers;

import com.qg.smpt.printer.LifecycleException;
import com.qg.smpt.printer.PrinterConnector;
import com.qg.smpt.receive.ReceOrderServlet;
import com.qg.smpt.util.OrderBuilder;
import com.qg.smpt.web.model.Order;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by tisong on 7/28/16.
 */
public class TestPrinterConnector implements Runnable{

    public static void main(String[] args) throws LifecycleException, InterruptedException {

        PrinterConnector printerConnector = new PrinterConnector();

        printerConnector.initialize();
        printerConnector.start();

        new Thread(new TestPrinterConnector()).start();

        TestPrinterConnector testPrinterConnector = new TestPrinterConnector();
        while(true) {
            synchronized (testPrinterConnector) {
                testPrinterConnector.wait();
            }
        }
    }


    @Override
    public synchronized void run() {
        OrderBuilder orderBuilder = new OrderBuilder();

        Order order1 = orderBuilder.produceOrder(false,false);
//        Order order2 = orderBuilder.produceOrder(false);
        ReceOrderServlet receOrderServlet = new ReceOrderServlet();

        try {
            receOrderServlet.doGet(1, order1);

            wait(1000);

            receOrderServlet.doGet(1, orderBuilder.produceOrder(false,false));
            wait(1000);
            receOrderServlet.doGet(1, orderBuilder.produceOrder(false,false));
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
