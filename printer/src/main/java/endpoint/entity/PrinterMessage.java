package endpoint.entity;

import util.transform.BytesConvert;

import java.util.Arrays;

/**
 * Created by tisong on 4/7/17.
 */
public class PrinterMessage {


    public final short start = Constants.statusStart;
    public short flag;

    public int  line1;

    public int  line2;

    public int  line3;

    public short checkSum;
    public final short end = Constants.statusEnd;

    public static PrinterMessage bytesToPrinterMessage(byte[] bytes) {
        PrinterMessage pm = new PrinterMessage();

        pm.flag = BytesConvert.bytesToShort(Arrays.copyOfRange(bytes, 2, 4));

        pm.line1 = BytesConvert.bytesToInt(Arrays.copyOfRange(bytes, 4, 8));

        pm.line2 = BytesConvert.bytesToInt(Arrays.copyOfRange(bytes, 8, 12));

        pm.line3 = BytesConvert.bytesToInt(Arrays.copyOfRange(bytes, 12, 16));

        pm.checkSum = BytesConvert.bytesToShort(Arrays.copyOfRange(bytes, 16, 18));

        return pm;
    }
}
