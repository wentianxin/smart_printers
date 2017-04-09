package com.qg.smpt.printer;

import com.qg.smpt.printer.connect.AbstractConnector;
import com.qg.smpt.printer.connect.SocketProcessorBase;
import com.qg.smpt.printer.util.collections.SynchronizedQueue;
import com.qg.smpt.printer.util.collections.SynchronizedStack;
import com.qg.smpt.printer.util.net.*;
import com.qg.smpt.share.ShareMem;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import com.qg.smpt.web.repository.PrinterMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by tisong on 7/20/16.
 */
public class PrinterConnector extends AbstractConnector{

    private ServerSocketChannel serverSock = null;

    private SynchronizedStack<NioChannel> nioChannels;



    public static final int OP_REGISTER = 0x100;

    protected class Acceptor extends AbstractConnector.Acceptor{

        @Override
        public void run() {
            while(running) {


                if (!running) {
                    break;
                }


                state = AcceptorState.RUNNING;

                try {
                    countUpOrAwaitConnection();

                    SocketChannel socket = null;
                    try {

                        socket = serverSock.accept();
                    } catch (IOException e) {
                        countDownConnection();
                        e.printStackTrace();
                    }

                    if (running && !paused) {
                        if (!setSocketOptions(socket)) {
                            countDownConnection();
                            closeSocket(socket);
                        }
                    } else {
                        countDownConnection();
                        closeSocket(socket);
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            state = AcceptorState.ENDED;
        }
    }

    protected boolean setSocketOptions(SocketChannel socket) {
        try {
            socket.configureBlocking(false);
            Socket sock = socket.socket();
            socketProperties.setProperties(sock);

            NioChannel channel = nioChannels.pop();
            if (channel == null) {
                SocketBufferHandler bufHandler = new SocketBufferHandler(
                        socketProperties.getAppReadBufSize(),
                        socketProperties.getAppWriteBufSize(),
                        socketProperties.getDirectBuffer());
                channel = new NioChannel(socket, bufHandler);
            } else {
                channel.setIOChannel(socket);
                channel.reset();
            }
            getPoller0().register(channel);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void closeSocket(SocketChannel socket) {

    }

    private Poller[] pollers = null;
    private AtomicInteger pollerRotater = new AtomicInteger(0);

    private volatile CountDownLatch stopLatch = null;

    private SynchronizedStack<PollerEvent> eventCache;

    public Poller getPoller0() {
        int idx = Math.abs(pollerRotater.incrementAndGet()) % pollers.length;
        return pollers[idx];
    }


    private long selectorTimeout = 1000;
    public void setSelectorTimeout(long timeout){ this.selectorTimeout = timeout;}
    public long getSelectorTimeout(){ return this.selectorTimeout; }

    public class Poller implements Runnable {

        private Selector selector;

        private volatile boolean close = false;

        private final SynchronizedQueue<PollerEvent> events = new SynchronizedQueue<>();

        private volatile int keyCount = 0;

        /**
         * wakeupCounter 的几个状态
         * 初始值: 0
         */
        private AtomicLong wakeupCounter = new AtomicLong(0);


        public Selector getSelector() {
            return selector;
        }


        @Override
        public void run() {

            while(true) {
                boolean hasEvents = false;

                try {
                    if (!close) {
                        hasEvents = events();
                        if (wakeupCounter.getAndSet(-1) > 0) {
                            // 说明有事件过来
                            keyCount = selector.selectNow();
                        } else {
                            keyCount = selector.select(selectorTimeout);
                        }
                        wakeupCounter.set(0);
                    }
                    if (close) {
                        events();
                        timeout(0, false);
                        try {
                            selector.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                    continue;
                }

                if (keyCount == 0) {
                    // 没有Selector感兴趣事件发生, 自己睡醒或者被主动唤醒, 再次检测是否因主动唤醒而有其他事件(PollerEvent)进入
                    hasEvents = (hasEvents | events());
                }

                Iterator<SelectionKey> iterator = keyCount > 0 ? selector.selectedKeys().iterator() : null;
                while (iterator != null && iterator.hasNext()) {
                    SelectionKey sk = iterator.next();
                    NioSocketWrapper attachment = (NioSocketWrapper) sk.attachment();
                    if (attachment == null) {
                        iterator.remove();
                    } else {
                        iterator.remove();
                        processKey(sk, attachment);
                    }
                }

                timeout(keyCount, hasEvents);
            }
        }


        public boolean events() {
            boolean result = false;

            PollerEvent pe = null;
            while ((pe = events.poll()) != null) {
                // TODO 倘若为null, 则直接结束
                result = true;
                try {
                    pe.run();
                    pe.reset();
                    if (running && !paused) {
                        eventCache.push(pe);
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

            return result;
        }

        public void register(final NioChannel socket) {
            socket.setPoller(this);

            NioSocketWrapper ka = new NioSocketWrapper(socket, PrinterConnector.this);
            socket.setSocketWrapper(ka);

            PollerEvent r = eventCache.pop();
            ka.interestOps(SelectionKey.OP_READ);
            if (r == null) {
                r = new PollerEvent(socket, ka, OP_REGISTER);
            } else {
                r.reset(socket, ka, OP_REGISTER);
            }
            addEvent(r);
        }

        private void addEvent(PollerEvent event) {
            events.offer(event);
            if (wakeupCounter.incrementAndGet() == 0) {
                // TODO 何时唤醒
                selector.wakeup();
            }
        }

        protected void timeout(int keyCount, boolean hasEvents) {
            System.out.println("超时暂未实现");
        }

        protected void processKey(SelectionKey sk, NioSocketWrapper attachment) {

            //processSocket(attachment, SocketEvent)
            try {
                if (close) {

                } else if (sk.isValid() && attachment != null) {
                    if (sk.isReadable() || sk.isWritable()) {
                        unreg(sk, attachment, sk.readyOps()); // 避免多个线程处理而造成混乱
                        boolean closeSocket = false;
                        if (sk.isReadable()) {
                            closeSocket = !processSocket(attachment, SocketEvent.OPEN_READ, true);
                        }
                        if (!closeSocket && sk.isWritable()) {
                            closeSocket = !processSocket(attachment, SocketEvent.OPEN_WRITE, true);
                        }
                        if (closeSocket) {
                            cancelledKey(sk);
                        }
                    }
                } else {
                    // 无效的key
                    cancelledKey(sk);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected void unreg(SelectionKey sk, NioSocketWrapper attachment, int readyOps) {
            //this is a must, so that we don't have multiple threads messing with the socket
            reg(sk, attachment, sk.interestOps() & (~readyOps));
        }

        protected void reg(SelectionKey sk, NioSocketWrapper attachment, int intops) {
            sk.interestOps(intops);
            attachment.interestOps(intops);
        }

        public NioSocketWrapper cancelledKey(SelectionKey key) {
            return null;
        }
    }

    public static class PollerEvent implements Runnable {

        private NioChannel socket;
        private int interestOps;
        private NioSocketWrapper socketWrapper;

        public PollerEvent(NioChannel ch, NioSocketWrapper w, int intOps) {
            reset(ch, w, intOps);
        }

        @Override
        public void run() {
            if (interestOps == OP_REGISTER) {
                try {
                    socket.getIOChannel().register(socket.getPoller().getSelector(), SelectionKey.OP_READ, socketWrapper);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("非注册事件");
            }
        }

        public void reset(NioChannel ch, NioSocketWrapper w, int intOps) {
            socket = ch;
            interestOps = intOps;
            socketWrapper = w;
        }

        public void reset() {
            reset(null, null, 0);
        }
    }

    protected class SocketProcessor extends SocketProcessorBase<NioChannel>{

        public SocketProcessor(SocketWrapperBase<NioChannel> socketWrapper, SocketEvent event) {
            super(socketWrapper, event);
        }


        @Override
        protected void doRun() {


            NioChannel socket = socketWrapper.getSocket();
            SelectionKey key = socket.getIOChannel().keyFor(socket.getPoller().getSelector()); // TODO keyFor函数

            try {
                Handler.SocketState state = Handler.SocketState.OPEN;

                if (event == null) {
                    state = getHandler().process(socketWrapper, SocketEvent.OPEN_READ);
                } else {
                    state = getHandler().process(socketWrapper, event);
                }

                if (state == Handler.SocketState.CLOSED) {
                    close(socket, key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                socketWrapper = null;
                event = null;
                //return to cache
                if (running && !paused) {
                    processorCache.push(this);
                }
            }
        }


    }

    private void close(NioChannel socket, SelectionKey key) {

    }

    private static final Logger LOGGER = Logger.getLogger(PrinterConnector.class.getName());

    private int curProcessors = 0;


    private int port = 8086;



    /* 关于生命周期的标志 */
    private boolean initizlized = false;

    /**
     * 已经创建但还未被使用
     */
    private Stack<PrinterProcessor> processors = new Stack<PrinterProcessor>();

    /**
     * 创建的所有线程
     */
    private List<PrinterProcessor> createdProcessor = new LinkedList<PrinterProcessor>();

    private ServerSocketChannel ssc = null;

    private Selector selector = null;





    @Override
    protected SocketProcessorBase createSocketProcessor(SocketWrapperBase socketWrapper, SocketEvent event) {
        return new SocketProcessor(socketWrapper, event);
    }



    /**
     *创建 ServerSocketChannel
     */
    public void initialize() {
        if (initizlized) {
            LOGGER.log(Level.ERROR, "PrinterConnector already initizlize");
        }

        //LOGGER.log(Level.DEBUG, "PrinterConnector initizlize, create serversocketchannel");
        try {
            ssc = ServerSocketChannel.open();

            ssc.configureBlocking(false);

            ssc.socket().bind(new InetSocketAddress(port));

            selector = Selector.open();

            ssc.register(selector, SelectionKey.OP_ACCEPT);
        } catch (final ClosedChannelException e) {
            LOGGER.log(Level.ERROR, "Initializes PrinterConnector failed", e);
        } catch (final IOException e) {
            LOGGER.log(Level.ERROR, "Initializes PrinterConnector failed", e);
        }
    }




    public void run() {
        // ServerSocketChannel 阻塞接收 SocketChannel
        // 将接收到的Socket（长连接），如果是读操作：分发给一个线程去处理。
        // 如果是写操作：写入订单数据，分发给同样的线程去处理 Processor

        SocketChannel sc = null;

        while (true) {
            try {
                selector.select();

                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    switch (key.readyOps()) {
                        case SelectionKey.OP_ACCEPT:
                            LOGGER.debug("ServerSocket accpet printer connection");
                            acceptSocket(key);
                          //  LOGGER.log(Level.DEBUG, "SocketChannel [{0}}",key.channel().toString());
                            break;
                        case SelectionKey.OP_READ:
                            // 当有多个 SocketChannel时, 会自动筛选哪一个SocketChannel 触发了事件
                            // 1. 连接后的一个请求：将打印机id-主控板（用户）id绑定，将打印机id-SocketChannel绑定
                            LOGGER.debug("ServerSocket accpet printer read request");
                            sc = (SocketChannel) key.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(20);
                            byteBuffer.clear();
                            /* 检测socket 客户端是否关闭 */
                            int nRead = sc.read(byteBuffer);
                            if ( nRead == -1 ) {
                                sc.close();
                                break;
                                // TODO 当打印机关闭连接时, 更新打印机状态, 打印机相关的共享内存对象?
                            }
                            byteBuffer.flip();

                         //   LOGGER.log(Level.DEBUG, "SocketChannel [{0}}",key.channel().toString());
                            PrinterProcessor processor = createProcessor();
                            processor.assign((SocketChannel)key.channel(), byteBuffer);
                           // LOGGER.log(Level.DEBUG, "ServerSocket accpet read requestion， alloate a printerProcessor thread id [{0}]", processor.getId());
                            break;
                        default: // something was wrong
                            LOGGER.log(Level.ERROR, "ServerSocket 出现未知情况");
                            break;
                    }

                    it.remove();
                }
            }catch (IOException e) {
                LOGGER.log(Level.ERROR, "暂时可忽略的错误 serverSocketChannel ", e);
                if (sc != null && sc.isOpen()) {
                    try {
                        sc.close();
                    } catch (IOException ee) {
                        LOGGER.log(Level.ERROR, "socket close exception", ee);
                    }
                }
            }
        }
    }

    /**
     * 创建processor实例（线程），并启动该线程，添加到已经创建的线程栈中
     * @return
     */
    private PrinterProcessor newProcessor() {

        PrinterProcessor processor = new PrinterProcessor(curProcessors++, this);
        try {
            processor.start();
        } catch (LifecycleException e) {

        }
        createdProcessor.add(processor);

        return processor;
    }


    private PrinterProcessor createProcessor() {

        if (processors.size() > 0) {
            return processors.pop();
        } else {
            return newProcessor();
        }
    }


    private void acceptSocket(SelectionKey key) {

        try {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();

            SocketChannel sc = server.accept();

            sc.configureBlocking(false);

            sc.register(selector, SelectionKey.OP_READ);

        } catch (final IOException e) {
            e.printStackTrace();
        }

    }


    public int getPort() {
        return port;
    }


    private int pollerThreadCount = Math.min(2,Runtime.getRuntime().availableProcessors());
    public void setPollerThreadCount(int pollerThreadCount) { this.pollerThreadCount = pollerThreadCount; }
    public int getPollerThreadCount() { return pollerThreadCount; }

    @Override
    public void bind() throws Exception {
        serverSock = ServerSocketChannel.open();
        socketProperties.setProperties(serverSock.socket());
        InetSocketAddress address = (getAddress() != null ? new InetSocketAddress(getAddress(), getPort()) :
            new InetSocketAddress(getPort()));

        serverSock.socket().bind(address, getBacklog());

        serverSock.socket().setSoTimeout(getSocketProperties().getSoTimeout());

        if (acceptorThreadCount == 0) {
            // FIXME: Doesn't seem to work that well with multiple accept threads
            acceptorThreadCount = 1;
        }
        if (pollerThreadCount <= 0) {
            //minimum one poller thread
            pollerThreadCount = 1;
        }

        stopLatch = new CountDownLatch(pollerThreadCount);
    }

    @Override
    public void unbind() throws Exception {

    }

    public void startInternal() throws Exception {
        if (!running) {
            running = true;
            paused  = false;

            processorCache = new SynchronizedStack<>(SynchronizedStack.DEFAULT_SIZE, socketProperties.getProcessorCache());
            eventCache = new SynchronizedStack<>(SynchronizedStack.DEFAULT_SIZE, socketProperties.getEventCache());
            nioChannels = new SynchronizedStack<>(SynchronizedStack.DEFAULT_SIZE,
                    socketProperties.getBufferPool());

            if (getExecutor() == null) {
                createExecutor();
            }

            //initializeConnectionLatch();

            pollers = new Poller[getPollerThreadCount()];
            for (int i = 0; i < pollers.length; i++) {
                pollers[i] = new Poller();
                Thread pollerThread = new Thread(pollers[i], getName() + "-ClientPoller-"+i);
                pollerThread.setPriority(threadPriority);
                pollerThread.setDaemon(true);
                pollerThread.start();
            }

            startAcceptorThreads();
        }

    }

    @Override
    public void stopInternal() throws Exception {

    }

    protected int acceptorThreadCount = 1;


    @Override
    protected AbstractConnector.Acceptor createAcceptor() {
        return new Acceptor();
    }


    public static class NioSocketWrapper extends SocketWrapperBase<NioChannel> {

        /**
         * 该Socket的事件
         */
        private int interestOps = 0;

        public NioSocketWrapper(NioChannel socket, AbstractConnector<NioChannel> endpoint) {
            super(socket, endpoint);
        }

        @Override
        public boolean isClosed() {
            return !getSocket().isOpen();
        }

        public int interestOps() { return interestOps;}
        public int  interestOps(int ops) { this.interestOps  = ops; return ops; }
    }

}
