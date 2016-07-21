package com.qg.smpt.printer;

/**
 * Created by tisong on 7/21/16.
 */
public interface Lifecycle {

    public void start() throws LifecycleException;

    public void stop() throws LifecycleException;
}
