package com.young.log.core.expression;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author: Young
 * @description: 日志函数解析
 **/

@Component
public class LogFunctionExecute {
    @Resource
    private IFunctionService functionService;

    public String getFunctionReturnValue(Map<String, String> funcNameAndReturnMap, Object value, String expression, String functionName){
        if(StringUtils.isEmpty(functionName)){
            return value == null ? "": value.toString();
        }
        String funcReturnValue = "";
        String funcCallInstanceKey = getFunctionCallInstanceKey(functionName, expression);
        if(funcNameAndReturnMap != null && funcNameAndReturnMap.containsKey(funcCallInstanceKey)){
            funcReturnValue = funcNameAndReturnMap.get(funcCallInstanceKey);
        } else {
            funcReturnValue = functionService.apply(functionName, value);
        }
        return funcReturnValue;
    }

    public boolean isBeforeFunction(String funcName){
        return functionService.isBeforeFunction(funcName);
    }

    public String getFunctionCallInstanceKey(String funcName, String expression){
        return  funcName + expression;
    }


}
