package com.young.log.core.dto;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @author: Young
 * @description: 方法执行结果
 **/
public class MethodExecuteResult {

    private boolean success;

    private Throwable throwable;

    private String errorMsg;

    private Object result;

    private final Method method;
    private final Object[] args;

    private final Class<?> targetClass;

    private final MethodInvocation invocation;

    public MethodExecuteResult(Method method, Object[] args, Class<?> targetClass, MethodInvocation invocation) {
        this.method = method;
        this.args = args;
        this.targetClass = targetClass;
        this.invocation = invocation;
    }

    public void executeFail(Throwable throwable, String errorMsg){
        this.success = false;
        this.throwable = throwable;
        this.errorMsg = errorMsg;
    }

    public void executeSuccess(Object result){
        this.success = true;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public MethodExecuteResult setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public MethodExecuteResult setThrowable(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public MethodExecuteResult setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public Object getResult() {
        return result;
    }

    public MethodExecuteResult setResult(Object result) {
        this.result = result;
        return this;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public MethodInvocation getInvocation() {
        return invocation;
    }
}
