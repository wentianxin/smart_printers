package endpoint;

import endpoint.entity.PrinterMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by tisong on 4/7/17.
 */
public class PrinterMessageDecoderHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.fireChannelRead(msgToPrinterMessage((ByteBuf)msg));
    }

    private PrinterMessage msgToPrinterMessage(ByteBuf msg) {
        return PrinterMessage.bytesToPrinterMessage(msg.array());
    }
}
