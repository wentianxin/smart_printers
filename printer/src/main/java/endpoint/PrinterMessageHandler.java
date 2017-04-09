package endpoint;

import endpoint.entity.PrinterMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import util.collections.SynchronizedStack;

import java.util.concurrent.Executor;

/**
 * Created by tisong on 4/7/17.
 */
public class PrinterMessageHandler extends ChannelInboundHandlerAdapter{

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        if (!(msg instanceof PrinterMessage)) {
            throw new RuntimeException("msg 必须属于 PrinterMessage");
        }

        PrinterMessage printerMessage = (PrinterMessage)msg;

        PrinterMessageProcessor processor = processorCache.pop();
        if (processor == null) {
            createPrinterMessageProcessor(printerMessage);
        } else {
            processor.reset(printerMessage);
        }

        Executor executor = getExecutor();

        executor.execute(processor);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

    }


    private Executor executor = null;

    private SynchronizedStack<PrinterMessageProcessor> processorCache;

    private PrinterMessageProcessor createPrinterMessageProcessor(PrinterMessage msg) {
        return new PrinterMessageProcessor(msg);
    }

    public Executor getExecutor() {
        return executor;
    }


}
