package com.young.log.core.handler;

import com.young.log.Enum.LogRecordErrorEnum;
import com.young.log.properties.EnableLogRecordProperties;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;

import static com.young.log.constants.LogRecordConstants.LOG_PREFIX;

/**
 * @author: Young
 * @description: 日志记录异常默认实现
 **/
@Slf4j
public class DefaultLogRecordErrorHandler implements ILogRecordErrorHandler{

    private final EnableLogRecordProperties enableLogRecordProperties;

    public DefaultLogRecordErrorHandler(EnableLogRecordProperties enableLogRecordProperties) {
        this.enableLogRecordProperties = enableLogRecordProperties;
    }

    @Override
    public void handlerError(LogRecordErrorEnum errorEnum, MethodInvocation invocation, Exception e) {
        if(enableLogRecordProperties.isLogOpenFlag()){
            log.error(LOG_PREFIX + "log record error handler invocation:{} e", invocation, e);
        }
    }
}
