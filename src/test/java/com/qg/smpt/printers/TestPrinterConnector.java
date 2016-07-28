package com.qg.smpt.printers;

import com.qg.smpt.printer.LifecycleException;
import com.qg.smpt.printer.PrinterConnector;

/**
 * Created by tisong on 7/28/16.
 */
public class TestPrinterConnector {

    public static void main(String[] args) throws LifecycleException, InterruptedException {

        PrinterConnector printerConnector = new PrinterConnector();

        printerConnector.initialize();
        printerConnector.start();

        TestPrinterConnector testPrinterConnector = new TestPrinterConnector();
        while(true) {
            synchronized (testPrinterConnector) {
                testPrinterConnector.wait();
            }
        }
    }
}
