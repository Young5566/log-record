package com.young.log.core.expression;

/**
 * @author: Young
 * @description: 自定义函数接口，实现此接口可自定义函数
 **/
public interface IFunctionService {

    String apply(String funcName, Object value);

    boolean isBeforeFunction(String funcName);
}
