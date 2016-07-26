package com.qg.smpt.printer;

/**
 *
 */
public class LifecycleException extends Exception{

    protected String message = null;

    protected Throwable throwable = null;


    public LifecycleException() {
        this(null, null);
    }

    public LifecycleException(String message) {
        this(message, null);
    }

    public LifecycleException(Throwable throwable) {
        this(null, throwable);
    }

    public LifecycleException(String message, Throwable throwable) {
        super();
        this.message = message;
        this.throwable = throwable;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("LifecycleException:  ");
        if (message != null) {
            sb.append(message);
            if (throwable != null) {
                sb.append(":  ");
            }
        }

        if (throwable != null) {
            sb.append(throwable.toString());
        }

        return sb.toString();
    }
}
