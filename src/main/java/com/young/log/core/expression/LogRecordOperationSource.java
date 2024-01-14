package com.young.log.core.expression;

import com.young.log.annotation.LogRecord;
import com.young.log.core.dto.LogRecordOps;
import com.young.log.core.exception.LogRecordException;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Young
 * @description: 日志记录操作源
 **/

@Component
public class LogRecordOperationSource {

    private final Map<AnnotatedElementKey, LogRecordOps> logRecordOpsCache = new ConcurrentHashMap<>();

    public LogRecordOps computeLogRecordOperate(Method method, Class<?> targetClass) {
        AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
        LogRecordOps ops = logRecordOpsCache.get(annotatedElementKey);
        if (ops != null) {
            return ops;
        }
        synchronized (logRecordOpsCache) {
            ops = logRecordOpsCache.get(annotatedElementKey);
            if (ops != null) {
                return ops;
            }
            LogRecord annotation = AnnotationUtils.getAnnotation(method, LogRecord.class);
            if (annotation == null) {
                throw new LogRecordException("获取LogRecord注解信息为空");
            }
            ops = getAnnotationOps(annotation);
            validateOpsLogContent(ops);
            logRecordOpsCache.put(annotatedElementKey, ops);
            return ops;
        }
    }

    private LogRecordOps getAnnotationOps(LogRecord annotation) {
        LogRecordOps logRecordOps = new LogRecordOps()
                .setSuccessLog(annotation.successLog())
                .setFailLog(annotation.failLog())
                .setCondition(annotation.condition())
                .setOperatorId(annotation.operatorId())
                .setOperateTypeCode(annotation.operateTypeCode())
                .setBizId(annotation.bizId())
                .setBizTypeCode(annotation.bizTypeCode())
                .setSuccessCondition(annotation.successCondition())
                .setAsync(annotation.isAsync())
                .setExtendClass(annotation.extendClass())
                .build();
        return logRecordOps;
    }

    private void validateOpsLogContent(LogRecordOps ops) {
        if (StringUtils.isEmpty(ops.getSuccessLog()) && StringUtils.isEmpty(ops.getFailLog())) {
            throw new LogRecordException("操作成功日志模板(successLog)和操作失败日志模板(failLog)不可同时为空");
        }
    }

}
