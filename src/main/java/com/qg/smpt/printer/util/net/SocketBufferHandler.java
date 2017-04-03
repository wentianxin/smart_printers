package com.qg.smpt.printer.util.net;

import java.nio.ByteBuffer;

/**
 * Created by tisong on 3/30/17.
 */
public class SocketBufferHandler {


    private volatile boolean readBufferConfiguredForWrite = true;
    private volatile ByteBuffer readBuffer;

    private volatile boolean writeBufferConfiguredForWrite = true;
    private volatile ByteBuffer writeBuffer;


    public void reset() {
        readBuffer.clear();
        readBufferConfiguredForWrite = true;
        writeBuffer.clear();
        writeBufferConfiguredForWrite = true;
    }
}
