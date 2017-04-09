package endpoint.entity;

/**
 * Created by tisong on 4/7/17.
 */
public final class PrinterStatus extends PrinterMessage{

    // line1
    public int mpuId; // 主控板id
    // line2
    public int seconds;   // 发送给服务器的时间戳
    // line3
    public int number;    // 主控板打印单元序号

    public static PrinterStatus bytesToPrinterStatus(PrinterMessage printerMessage) {

        PrinterStatus printerStatus = new PrinterStatus();

        printerStatus.mpuId = printerMessage.line1;

        printerStatus.seconds = printerMessage.line2;

        printerStatus.number = printerMessage.line3;

        printerStatus.checkSum = 0;

        return printerStatus;
    }

}
