package util.factory;

import eneity.state.PrinterStateMachine;

/**
 * Created by tisong on 4/7/17.
 */
public class PrinterStateMachineSingleton {


    private PrinterStateMachineSingleton() {}

    public static PrinterStateMachine getPrinterStateMachine() {
        return PrinterStateMachineSingletonHolder.PRINTERSTATEMACHINE;
    }

    private static class PrinterStateMachineSingletonHolder {
        private static final PrinterStateMachine PRINTERSTATEMACHINE = new PrinterStateMachine();
    }
}
