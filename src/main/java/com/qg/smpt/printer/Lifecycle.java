package com.qg.smpt.printer;

/**
 * 生命周期
 */
public interface Lifecycle {

    public void start() throws LifecycleException;

    public void stop() throws LifecycleException;
}
