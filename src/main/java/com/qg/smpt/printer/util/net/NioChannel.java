package com.qg.smpt.printer.util.net;

import com.qg.smpt.printer.PrinterConnector;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;

import com.qg.smpt.printer.PrinterConnector.Poller;

/**
 * Created by tisong on 3/30/17.
 */
public class NioChannel implements ByteChannel {

    protected SocketChannel sc;

    protected final SocketBufferHandler bufferHandler;

    public NioChannel (SocketChannel channel, SocketBufferHandler bufferHandler) {
        this.sc = channel;
        this.bufferHandler = bufferHandler;
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        return 0;
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        return 0;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void close() throws IOException {

    }

    public void reset() throws IOException {
        bufferHandler.reset();
    }

    public void setIOChannel(SocketChannel channel) {
        this.sc = channel;
    }

    public SocketChannel getIOChannel() {
        return this.sc;
    }


    private Poller poller;

    public void setPoller(Poller poller) {
        this.poller = poller;
    }
    public Poller getPoller() {
        return poller;
    }
}
