package com.young.log.core.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Young
 * @description: 日志记录线程池工厂
 **/
public class LogRecordExecuteThreadFactory implements ThreadFactory {

    private static final AtomicInteger threadInitNum = new AtomicInteger();

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, "Log-Record-Thread-" + threadInitNum.getAndIncrement());
    }
}
