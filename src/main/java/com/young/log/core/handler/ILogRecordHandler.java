package com.young.log.core.handler;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @author: Young
 * @description: 日志记录目标方法拦截器
 **/
public interface ILogRecordHandler {

    Object execute(MethodInvocation invocation, Object target, Method method, Object[] args) throws Throwable;
}
