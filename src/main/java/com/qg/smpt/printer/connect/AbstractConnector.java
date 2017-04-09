package com.qg.smpt.printer.connect;

import com.qg.smpt.printer.util.collections.SynchronizedStack;
import com.qg.smpt.printer.util.net.SocketEvent;
import com.qg.smpt.printer.util.net.SocketProperties;
import com.qg.smpt.printer.util.net.SocketWrapperBase;
import com.qg.smpt.printer.util.threads.LimitLatch;
import com.qg.smpt.printer.util.threads.TaskQueue;
import com.qg.smpt.printer.util.threads.TaskThreadFactory;
import com.qg.smpt.printer.util.threads.ThreadPoolExecutor;

import java.net.InetAddress;
import java.util.Set;
import java.util.concurrent.Executor;

import java.util.concurrent.TimeUnit;

/**
 * Created by tisong on 3/30/17.
 */
public abstract class AbstractConnector<S> {

    public abstract static class Acceptor implements Runnable {
        public enum AcceptorState {
            NEW, RUNNING, PAUSED, ENDED
        }

        protected volatile AcceptorState state = AcceptorState.NEW;
        public final AcceptorState getState() {
            return state;
        }

        private String threadName;
        protected final void setThreadName(final String threadName) {
            this.threadName = threadName;
        }
        protected final String getThreadName() {
            return threadName;
        }
    }

    protected SynchronizedStack<SocketProcessorBase> processorCache;

    public boolean processSocket(SocketWrapperBase<S> socketWrapper, SocketEvent event, boolean dispatch) {
        try {
            SocketProcessorBase<S> sc = processorCache.pop();
            if (sc == null) {
                sc = createSocketProcessor(socketWrapper, event);
            } else {
                sc.reset(socketWrapper, event);
            }

            Executor executor = getExecutor();
            if (dispatch && executor != null) {
                executor.execute(sc);
            } else {
                sc.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected abstract SocketProcessorBase<S> createSocketProcessor(SocketWrapperBase<S> socketWrapper, SocketEvent event);



    protected SocketProperties socketProperties = new SocketProperties();

    public SocketProperties getSocketProperties() {
        return socketProperties;
    }


    private Executor executor = null;
    public Executor getExecutor() {
        return executor;
    }

    public void createExecutor() {
        internalExecutor = true;
        TaskQueue taskQueue = new TaskQueue();
        TaskThreadFactory tf = new TaskThreadFactory(getName() + "-exec-", daemon, getThreadPriority());
        executor = new ThreadPoolExecutor(getMinSpareThreads(), getMaxThreads(), 60, TimeUnit.SECONDS, taskQueue, tf);
        taskQueue.setParent((ThreadPoolExecutor)executor);
    }

    private int minSpareThreads = 10;
    public int getMinSpareThreads() {
        return Math.min(minSpareThreads,getMaxThreads());
    }

    /**
     * Maximum amount of worker threads.
     */
    private int maxThreads = 200;
    public int getMaxThreads() {
        return maxThreads;
    }

    /**
     * Handling of accepted sockets.
     */
    private Handler<S> handler = null;
    public void setHandler(Handler<S> handler ) { this.handler = handler; }
    public Handler<S> getHandler() { return handler; }

    protected enum BindState {
        UNBOUND, BOUND_ON_INIT, BOUND_ON_START
    }

    public static interface Handler<S> {

        /**
         * Different types of socket states to react upon.
         */
        public enum SocketState {
            // TODO Add a new state to the AsyncStateMachine and remove
            //      ASYNC_END (if possible)
            OPEN, CLOSED, LONG, ASYNC_END, SENDFILE, UPGRADING, UPGRADED
        }


        /**
         * Process the provided socket with the given current status.
         *
         * @param socket The socket to process
         * @param status The current socket status
         *
         * @return The state of the socket after processing
         */
        public SocketState process(SocketWrapperBase<S> socket,
                                   SocketEvent status);


        /**
         * Obtain the GlobalRequestProcessor associated with the handler.
         *
         * @return the GlobalRequestProcessor
         */
        public Object getGlobal();


        /**
         * Obtain the currently open sockets.
         *
         * @return The sockets for which the handler is tracking a currently
         *         open connection
         */
        public Set<S> getOpenSockets();

        /**
         * Release any resources associated with the given SocketWrapper.
         *
         * @param socketWrapper The socketWrapper to release resources for
         */
        public void release(SocketWrapperBase<S> socketWrapper);


        /**
         * Inform the handler that the endpoint has stopped accepting any new
         * connections. Typically, the endpoint will be stopped shortly
         * afterwards but it is possible that the endpoint will be resumed so
         * the handler should not assume that a stop will follow.
         */
        public void pause();


        /**
         * Recycle resources associated with the handler.
         */
        public void recycle();
    }



    protected volatile boolean running = false;
    protected volatile boolean paused  = false;

    protected volatile boolean internalExecutor = false;

    private volatile LimitLatch connectionLimitLatch = null;
    protected LimitLatch initializeConnectionLatch() {
        if(maxConnections == -1) {
            return null;
        }
        if (connectionLimitLatch == null) {
            connectionLimitLatch = new LimitLatch(getMaxConnections());
        }
        return connectionLimitLatch;
    }
    protected void countUpOrAwaitConnection() throws InterruptedException {
        if (maxConnections == -1) {
            return ;
        }
        LimitLatch latch = connectionLimitLatch;
        if (latch != null) {
            latch.countUpOrAwait();
        }
    }

    protected long countDownConnection() {
        if (maxConnections == -1) {
            return -1;
        }

        LimitLatch latch = connectionLimitLatch;
        if (latch == null) {
            return -1;
        }

        long result = latch.countDown();
        if (result < 0) {
            System.out.println("endpoint.warn.incorrectConnectionCount");
        }
        return result;
    }

    protected void releaseConnectionLatch() {
        LimitLatch latch = connectionLimitLatch;
        if (latch!=null) latch.releaseAll();
        connectionLimitLatch = null;
    }


    protected Acceptor[] acceptors;

    protected int acceptorThreadPriority = Thread.NORM_PRIORITY;
    public void setAcceptorThreadPriority(int acceptorThreadPriority) {
        this.acceptorThreadPriority = acceptorThreadPriority;
    }
    public int getAcceptorThreadPriority() { return acceptorThreadPriority; }

    protected int threadPriority = Thread.NORM_PRIORITY;
    public void setThreadPriority(int threadPriority) { this.threadPriority = threadPriority; }
    public int getThreadPriority() { return threadPriority; }


    private InetAddress address;
    public InetAddress getAddress() { return address; }
    public void setAddress(InetAddress address) { this.address = address; }

    /**
     * Allows the server developer to specify the backlog that
     * should be used for server sockets. By default, this value
     * is 100.
     */
    private int backlog = 100;
    public void setBacklog(int backlog) { if (backlog > 0) this.backlog = backlog; }
    public int getBacklog() { return backlog; }


    /**
     * Name of the thread pool, which will be used for naming child threads.
     */
    private String name = "TP";
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    /**
     * The default is true - the created threads will be
     *  in daemon mode. If set to false, the control thread
     *  will not be daemon - and will keep the process alive.
     */
    private boolean daemon = true;
    public void setDaemon(boolean b) { daemon = b; }
    public boolean getDaemon() { return daemon; }


    public void setTcpNoDelay(boolean tcpNoDelay) { socketProperties.setTcpNoDelay(tcpNoDelay); }



    protected int acceptorThreadCount = 1;
    public void setAcceptorThreadCount(int acceptorThreadCount) {
        this.acceptorThreadCount = acceptorThreadCount;
    }
    public int getAcceptorThreadCount() { return acceptorThreadCount; }



    public abstract void bind() throws Exception;
    public abstract void unbind() throws Exception;
    public abstract void startInternal() throws Exception;
    public abstract void stopInternal() throws Exception;


    private boolean bindOnInit = true;
    private volatile BindState bindState = BindState.UNBOUND;

    public void init() throws Exception {
        if (bindOnInit) {
            bind();
            bindState = BindState.BOUND_ON_INIT;
        }
    }

    public final void start() throws Exception {
        if (bindState == BindState.UNBOUND) {
            bind();
            bindState = BindState.BOUND_ON_START;
        }
        startInternal();
    }

    protected final void startAcceptorThreads() {
        int count = getAcceptorThreadCount();
        acceptors = new Acceptor[count];

        for (int i = 0; i < count; i++) {
            acceptors[i] = createAcceptor();
            String threadName = getName() + "-Acceptor-" + i;
            Thread t = new Thread(acceptors[i], threadName);
            t.setPriority(getAcceptorThreadPriority());
            t.setDaemon(getDaemon());
            t.start();
        }
    }

    public final void stop() throws Exception {

    }

    public final void destory() throws Exception {

    }


    protected abstract Acceptor createAcceptor();

    public void setSoLinger(int soLinger) {
        socketProperties.setSoLingerTime(soLinger);
        socketProperties.setSoLingerOn(soLinger>=0);
    }

    private int maxConnections = 10000;
    public void setMaxConnections(int maxCon) {
        this.maxConnections = maxCon;
        LimitLatch latch = this.connectionLimitLatch;
        if (latch != null) {
            // Update the latch that enforces this
            if (maxCon == -1) {
                releaseConnectionLatch();
            } else {
                latch.setLimit(maxCon);
            }
        } else if (maxCon > 0) {
            initializeConnectionLatch();
        }
    }

    public int getMaxConnections() {
        return maxConnections;
    }
}
