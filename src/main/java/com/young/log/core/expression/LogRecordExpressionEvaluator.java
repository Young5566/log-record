package com.young.log.core.expression;

import org.springframework.aop.support.AopUtils;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Young
 * @description: 日志记录表达式执行器
 **/
@Component
public class LogRecordExpressionEvaluator extends CachedExpressionEvaluator {

    private final Map<ExpressionKey, Expression> expressionCache = new ConcurrentHashMap<>(64);

    private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);

    public String parseExpression(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evaluationContext){
        return getExpression(this.expressionCache, methodKey, conditionExpression).getValue(evaluationContext, String.class);
    }

    public Method getTargetMethod(Class<?> targetClass, Method method){
        AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
        return targetMethodCache.computeIfAbsent(annotatedElementKey, k-> AopUtils.getMostSpecificMethod(method, targetClass));
    }


}
