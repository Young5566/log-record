package com.young.log.core.handler;

import com.young.log.properties.EnableLogRecordProperties;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Map;

import static com.young.log.constants.LogRecordConstants.LOG_PREFIX;

/**
 * @author: Young
 * @description: 日志记录扩展默认实现
 **/
@Slf4j
public class DefaultLogRecordExtendHandler implements ILogRecordExtendHandler{

    private final EnableLogRecordProperties enableLogRecordProperties;

    public DefaultLogRecordExtendHandler(EnableLogRecordProperties enableLogRecordProperties) {
        this.enableLogRecordProperties = enableLogRecordProperties;
    }

    @Override
    public void beforeHandler(MethodInvocation invocation, Map<String, Object> extInfo) {
        if(enableLogRecordProperties.isLogOpenFlag()){
            log.info(LOG_PREFIX + "log record target method proceed beforeHandler invocation:{}", invocation);
        }
    }

    @Override
    public void afterHandler(MethodInvocation invocation, Map<String, Object> extInfo) {
        if(enableLogRecordProperties.isLogOpenFlag()){
            log.info(LOG_PREFIX + "log record target method proceed afterHandler invocation:{} e", invocation);
        }
    }
}
