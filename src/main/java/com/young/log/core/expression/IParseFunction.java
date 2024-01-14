package com.young.log.core.expression;

/**
 * @author: Young
 * @description: 自定义解析函数
 **/
public interface IParseFunction {

    default boolean executeBefore(){
        return false;
    }

    String functionName();

    String apply(String value);

}
