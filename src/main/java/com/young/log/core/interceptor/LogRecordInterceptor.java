package com.young.log.core.interceptor;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.young.log.core.handler.LogRecordHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Stack;

/**
 * @author: Young
 * @description: 方法拦截器
 **/

public class LogRecordInterceptor implements MethodInterceptor {

    private LogRecordHandler logRecordHandler;

    public LogRecordInterceptor(LogRecordHandler logRecordHandler) {
        this.logRecordHandler = logRecordHandler;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        return logRecordHandler.execute(methodInvocation, methodInvocation.getThis(), methodInvocation.getMethod(), methodInvocation.getArguments());
    }
}
