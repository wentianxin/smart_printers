package com.qg.smpt.printer.util.net;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by tisong on 3/31/17.
 */
public class SocketProperties {

    /**
     * Enable/disable socket processor cache, this bounded cache stores
     * SocketProcessor objects to reduce GC
     * Default is 500
     * -1 is unlimited
     * 0 is disabled
     * 避免FULL GC
     */
    protected int processorCache = 500;

    protected int eventCache     = 500;

    /**
     * NioChannel pool size for the endpoint,
     * this value is how many channels
     * -1 means unlimited cached, 0 means no cache
     * Default value is 500
     */
    protected int bufferPool = 500;




    /**
     * Socket receive buffer size in bytes (SO_RCVBUF).
     * JVM default used if not set.
     */
    protected Integer rxBufSize = null;

    /**
     * Socket send buffer size in bytes (SO_SNDBUF).
     * JVM default used if not set.
     */
    protected Integer txBufSize = null;


    protected Boolean soKeepAlive = null;


    protected Integer performanceConnectionTime = null;

    protected Integer performanceLatency = null;

    protected Integer performanceBandwidth = null;

    protected Integer soTimeout = Integer.valueOf(20000);

    protected Boolean tcpNoDelay = null;

    protected Boolean ooBInline = null;

    protected Boolean soLingerOn = null;

    protected Integer soLingerTime = null;

    protected Boolean soReuseAddress = null;

    public void setProperties(Socket socket) throws SocketException {
        if (rxBufSize != null)
            socket.setReceiveBufferSize(rxBufSize.intValue());
        if (txBufSize != null)
            socket.setSendBufferSize(txBufSize.intValue());
        if (ooBInline !=null)
            socket.setOOBInline(ooBInline.booleanValue());
        if (soKeepAlive != null)
            socket.setKeepAlive(soKeepAlive.booleanValue());
        if (performanceConnectionTime != null && performanceLatency != null &&
                performanceBandwidth != null)
            socket.setPerformancePreferences(
                    performanceConnectionTime.intValue(),
                    performanceLatency.intValue(),
                    performanceBandwidth.intValue());
        if (soReuseAddress != null)
            socket.setReuseAddress(soReuseAddress.booleanValue());
        if (soLingerOn != null && soLingerTime != null)
            socket.setSoLinger(soLingerOn.booleanValue(),
                    soLingerTime.intValue());
        if (soTimeout != null && soTimeout.intValue() >= 0)
            socket.setSoTimeout(soTimeout.intValue());
        if (tcpNoDelay != null)
            socket.setTcpNoDelay(tcpNoDelay.booleanValue());
    }

    public void setProperties(ServerSocket sockServer) {

    }


    public Boolean getSoKeepAlive() {
        return soKeepAlive;
    }

    public int getEventCache() {
        return eventCache;
    }

    public int getProcessorCache() {
        return processorCache;
    }

    public Integer getRxBufSize() {
        return rxBufSize;
    }

    public Integer getTxBufSize() {
        return txBufSize;
    }

    public int getBufferPool() {
        return bufferPool;
    }

    public Integer getSoTimeout() {
        return soTimeout;
    }

    public Boolean getTcpNoDelay() {
        return tcpNoDelay;
    }

    public Boolean getOoBInline() {
        return ooBInline;
    }

    public Boolean getSoLingerOn() {
        return soLingerOn;
    }

    public Integer getSoLingerTime() {
        return soLingerTime;
    }

    public Boolean getSoReuseAddress() {
        return soReuseAddress;
    }

    public Integer getPerformanceConnectionTime() {
        return performanceConnectionTime;
    }

    public Integer getPerformanceLatency() {
        return performanceLatency;
    }

    public Integer getPerformanceBandwidth() {
        return performanceBandwidth;
    }


    public void setProcessorCache(int processorCache) {
        this.processorCache = processorCache;
    }

    public void setEventCache(int eventCache) {
        this.eventCache = eventCache;
    }

    public void setBufferPool(int bufferPool) {
        this.bufferPool = bufferPool;
    }

    public void setRxBufSize(Integer rxBufSize) {
        this.rxBufSize = rxBufSize;
    }

    public void setTxBufSize(Integer txBufSize) {
        this.txBufSize = txBufSize;
    }

    public void setSoKeepAlive(Boolean soKeepAlive) {
        this.soKeepAlive = soKeepAlive;
    }

    public void setPerformanceConnectionTime(Integer performanceConnectionTime) {
        this.performanceConnectionTime = performanceConnectionTime;
    }

    public void setPerformanceLatency(Integer performanceLatency) {
        this.performanceLatency = performanceLatency;
    }

    public void setPerformanceBandwidth(Integer performanceBandwidth) {
        this.performanceBandwidth = performanceBandwidth;
    }

    public void setSoTimeout(Integer soTimeout) {
        this.soTimeout = soTimeout;
    }

    public void setTcpNoDelay(Boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public void setOoBInline(Boolean ooBInline) {
        this.ooBInline = ooBInline;
    }

    public void setSoLingerOn(Boolean soLingerOn) {
        this.soLingerOn = soLingerOn;
    }

    public void setSoLingerTime(Integer soLingerTime) {
        this.soLingerTime = soLingerTime;
    }

    public void setSoReuseAddress(Boolean soReuseAddress) {
        this.soReuseAddress = soReuseAddress;
    }
}
