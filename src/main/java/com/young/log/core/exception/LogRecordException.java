package com.young.log.core.exception;

/**
 * @author: Young
 * @description: 日志记录异常
 **/
public class LogRecordException extends RuntimeException{

    public LogRecordException() {
    }

    public LogRecordException(String message) {
        super(message);
    }

    public LogRecordException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogRecordException(Throwable cause) {
        super(cause);
    }

    public LogRecordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
