package com.young.log.core.expression;

import com.young.log.core.factory.ParseFunctionFactory;

/**
 * @author: Young
 * @description: 自定义函数默认实现
 **/
public class DefaultFunctionService implements IFunctionService{

    private final ParseFunctionFactory parseFunctionFactory;

    public DefaultFunctionService(ParseFunctionFactory parseFunctionFactory) {
        this.parseFunctionFactory = parseFunctionFactory;
    }

    @Override
    public String apply(String funcName, Object value) {
        IParseFunction func = parseFunctionFactory.getFunction(funcName);
        if(func == null){
            return value.toString();
        }
        return func.apply(value.toString());
    }

    @Override
    public boolean isBeforeFunction(String funcName) {
        return parseFunctionFactory.isBeforeFunction(funcName);
    }
}
