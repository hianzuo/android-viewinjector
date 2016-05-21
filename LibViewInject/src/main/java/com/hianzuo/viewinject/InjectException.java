package com.hianzuo.viewinject;

/**
 * User: Ryan
 * Date: 14-3-31
 * Time: 上午11:46
 */
public class InjectException extends RuntimeException {
    public InjectException() {
        super();
    }

    public InjectException(String detailMessage) {
        super(detailMessage);
    }

    public InjectException(Throwable throwable) {
        super(throwable);
    }

    public InjectException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
