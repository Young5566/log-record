package com.young.log.core.expression;

import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * @author: Young
 * @description: 操作日志值解析
 **/
@Component
public class LogRecordValueParser {

    @Resource
    private LogRecordExpressionEvaluator evaluator;



    public LogRecordValueParser(LogRecordExpressionEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public EvaluationContext createEvaluationContext(Method method, Object[] arguments, Class<?> targetClass, Object ret, String errorMsg){
        Method targetMethod = evaluator.getTargetMethod(targetClass, method);
        return new LogRecordEvaluationContext(null, targetMethod, arguments, new DefaultParameterNameDiscoverer(), ret, errorMsg);
    }

    public String getExpression(String conditionExpression, AnnotatedElementKey annotatedElementKey, EvaluationContext evaluationContext){
        return evaluator.parseExpression(conditionExpression, annotatedElementKey, evaluationContext);
    }
}
