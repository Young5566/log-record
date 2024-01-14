package com.young.log.core.factory;

import com.young.log.core.expression.IParseFunction;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Young
 * @description: 自定义函数工厂
 **/
public class ParseFunctionFactory {

    private Map<String, IParseFunction> allFuncMap;


    public ParseFunctionFactory(List<IParseFunction> functionList){
        if(CollectionUtils.isEmpty(functionList)){
            return;
        }
        allFuncMap = new HashMap<>();
        for (IParseFunction func: functionList) {
            if(StringUtils.isEmpty(func.functionName())) continue;
            allFuncMap.put(func.functionName(), func);
        }
    }

    public IParseFunction getFunction(String functionName){
        return allFuncMap.get(functionName);
    }

    public boolean isBeforeFunction(String functionName){
        return allFuncMap.get(functionName) != null && allFuncMap.get(functionName).executeBefore();
    }


}
