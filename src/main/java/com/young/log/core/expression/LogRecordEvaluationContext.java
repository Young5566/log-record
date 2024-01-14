package com.young.log.core.expression;

import com.young.log.core.LogRecordContext;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Young
 * @description: 表达式执行上下文缓存
 **/
public class LogRecordEvaluationContext extends MethodBasedEvaluationContext {
    public LogRecordEvaluationContext(Object rootObject, Method method, Object[] arguments, ParameterNameDiscoverer parameterNameDiscoverer, Object ret, Object errorMsg) {
        super(rootObject, method, arguments, parameterNameDiscoverer);

        setVariable("_ret", ret);
        setVariable("errorMsg", errorMsg);

        Map<String, Object> variables = LogRecordContext.getVariables();
        if(!CollectionUtils.isEmpty(variables)){
            variables.forEach(this::setVariable);
        }
    }
}
