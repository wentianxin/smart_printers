package com.qg.smpt.printer.connect;

import com.qg.smpt.printer.util.net.SocketEvent;
import com.qg.smpt.printer.util.net.SocketWrapperBase;

import java.util.Set;

/**
 * Created by tisong on 3/31/17.
 */
public class AbstractProtocol<S> {

    protected static class ConnectionHandler<S> implements AbstractConnector.Handler<S> {

        @Override
        public SocketState process(SocketWrapperBase<S> wrapper, SocketEvent status) {
            if (wrapper == null) {
                return SocketState.CLOSED;
            }

            S socket = wrapper.getSocket(); // NioChannel

            Pocessor processor = connections.get(socket);
            if ((status == SocketEvent.DISCONNECT || status == SocketEvent.ERROR)
                    && processor == null) {
                // Nothing to do. Endpoint requested a close and there is no
                // longer a processor associated with this socket.
                return SocketState.CLOSED;
            }
        }

        @Override
        public Object getGlobal() {
            return null;
        }

        @Override
        public Set<S> getOpenSockets() {
            return null;
        }

        @Override
        public void release(SocketWrapperBase<S> socketWrapper) {

        }

        @Override
        public void pause() {

        }

        @Override
        public void recycle() {

        }
    }

    // Default protocol settings
    public static final int DEFAULT_CONNECTION_LINGER = -1;
    public static final boolean DEFAULT_TCP_NO_DELAY = true;

    private final AbstractConnector<S> endpoint;

    public AbstractProtocol(AbstractConnector<S> endpoint) {
        this.endpoint = endpoint;
        setSoLinger(DEFAULT_CONNECTION_LINGER);
        setTcpNoDelay(DEFAULT_TCP_NO_DELAY);
    }


    public void init() throws Exception {
        try {
            endpoint.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws Exception {
        try {
            endpoint.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setSoLinger(int soLinger) { endpoint.setSoLinger(soLinger); }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        endpoint.setTcpNoDelay(tcpNoDelay);
    }
}
