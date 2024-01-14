package com.young.log.core.handler;

import com.young.log.Enum.LogRecordErrorEnum;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author: Young
 * @description: 日志记录异常处理
 **/
public interface ILogRecordErrorHandler {

    void handlerError(LogRecordErrorEnum errorEnum, MethodInvocation invocation, Exception e);

}
