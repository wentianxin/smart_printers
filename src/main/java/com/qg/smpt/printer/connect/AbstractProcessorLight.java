package com.qg.smpt.printer.connect;

import com.qg.smpt.printer.connect.AbstractConnector.Handler.SocketState;
import com.qg.smpt.printer.util.net.SocketEvent;
import com.qg.smpt.printer.util.net.SocketWrapperBase;

/**
 * Created by tisong on 4/1/17.
 */
public class AbstractProcessorLight {

    public SocketState process (SocketWrapperBase<?> socketWrapper, SocketEvent status) {
        return null;
    }
}
