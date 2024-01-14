package com.young.log.core.handler;

import org.aopalliance.intercept.MethodInvocation;

import java.util.Map;

/**
 * @author: Young
 * @description: 扩展接口，各业务模块可实现此接口进行特殊逻辑处理
 **/
public interface ILogRecordExtendHandler {

    void beforeHandler(MethodInvocation invocation, Map<String, Object> extInfo);

    void afterHandler(MethodInvocation invocation, Map<String, Object> extInfo);

}
