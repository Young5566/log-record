package com.young.log.core;

import java.util.UUID;

/**
 * @author: Young
 * @description: 获取日志唯一id默认实现
 **/
public class DefaultLogRecordIdGetService implements ILogRecordIdGetService {

    @Override
    public String getLogUniqueId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
