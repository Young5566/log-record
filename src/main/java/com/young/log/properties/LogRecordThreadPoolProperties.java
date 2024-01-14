package com.young.log.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: Young
 * @description: 线程池配置
 **/

@ConfigurationProperties(prefix = "log-record.thread-pool")
public class LogRecordThreadPoolProperties {

    private int corePoolSize = 6;

    private int MaxPoolSize = 6;

    private int queueCapacity = 500;

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public LogRecordThreadPoolProperties setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        return this;
    }

    public int getMaxPoolSize() {
        return MaxPoolSize;
    }

    public LogRecordThreadPoolProperties setMaxPoolSize(int maxPoolSize) {
        MaxPoolSize = maxPoolSize;
        return this;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public LogRecordThreadPoolProperties setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
        return this;
    }
}
