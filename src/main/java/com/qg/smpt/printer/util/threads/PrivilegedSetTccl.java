package com.qg.smpt.printer.util.threads;

import java.security.PrivilegedAction;

public class PrivilegedSetTccl implements PrivilegedAction<Void> {

    private ClassLoader cl;

    public PrivilegedSetTccl(ClassLoader cl) {
        this.cl = cl;
    }

    @Override
    public Void run() {
        Thread.currentThread().setContextClassLoader(cl);
        return null;
    }
}