package com.qg.smpt.printer.util.threads;

/**
 * Static constants for this package.
 */
public final class Constants {

    public static final long DEFAULT_THREAD_RENEWAL_DELAY = 1000L;

    /**
     * Has security been turned on?
     */
    public static final boolean IS_SECURITY_ENABLED = (System.getSecurityManager() != null);
}
