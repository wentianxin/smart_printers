import endpoint.PrinterMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by tisong on 4/7/17.
 */
public class Starter {

    public static void main(String[] args) throws InterruptedException {


        EventLoopGroup acceptor = new NioEventLoopGroup();
        EventLoopGroup worker   = new NioEventLoopGroup(4);

        try {
            ServerBootstrap start = new ServerBootstrap();

            start.group(acceptor, worker).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            System.out.println("打印机接入");
                            ch.pipeline().addLast(new TestHandler());
                            ch.pipeline().addLast(new PrinterMessageHandler());

                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = start.bind(8080).sync();
            future.channel().closeFuture().sync();
        } finally {
            acceptor.shutdownGracefully().sync();
            worker.shutdownGracefully().sync();
        }
    }
}
