package com.young.log.core.handler;

import com.alibaba.ttl.TtlRunnable;
import com.young.log.Enum.LogRecordErrorEnum;
import com.young.log.annotation.LogRecord;
import com.young.log.core.ILogRecordIdGetService;
import com.young.log.core.IOperatorGetService;
import com.young.log.core.LogRecordContext;
import com.young.log.core.dto.*;
import com.young.log.core.exception.LogRecordException;
import com.young.log.core.expression.LogFunctionExecute;
import com.young.log.core.expression.LogRecordOperationSource;
import com.young.log.core.expression.LogRecordValueParser;
import com.young.log.core.factory.LogRecordDtoFactory;
import com.young.log.core.record.ILogRecordService;
import com.young.log.properties.EnableLogRecordProperties;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.young.log.constants.LogRecordConstants.LOG_PREFIX;

/**
 * @author: Young
 * @description: 日志记录拦截器实现
 **/

@Slf4j
@Component
public class LogRecordHandler implements ILogRecordHandler {

    private static final Pattern pattern = Pattern.compile("\\{\\s*(\\w*)\\s*\\{(.*?)}}");

    @Resource
    private EnableLogRecordProperties enableLogRecordProperties;

    @Resource
    private Executor ttlLogRecordExecutor;

    @Resource
    private LogRecordOperationSource logRecordOperationSource;

    @Resource
    private IOperatorGetService operatorGetService;

    @Resource
    private LogRecordValueParser logRecordValueParser;

    @Resource
    private LogFunctionExecute logFunctionExecute;

    @Resource
    private ILogRecordService logRecordService;

    @Resource
    private LogRecordDtoFactory logRecordDtoFactory;

    @Resource
    private ILogRecordErrorHandler logRecordErrorHandler;

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private ILogRecordIdGetService logRecordIdGetService;

    @Override
    public Object execute(MethodInvocation invocation, Object target, Method method, Object[] args) throws Throwable {
        if (AopUtils.isAopProxy(target)) {
            invocation.proceed();
        }

        Class<?> targetClass = AopUtils.getTargetClass(target);
        MethodExecuteResult methodExecuteResult = new MethodExecuteResult(method, args, targetClass, invocation);
        LogRecordContext.pushEmptySpan();
        Object ret = null;
        LogRecordOps ops = null;
        Map<String, String> funcNameAndReturnMap = new HashMap<>();
        Map<String, Object> extInfo = new HashMap<>();
        ILogRecordExtendHandler logRecordExtendHandler = getLogRecordExtendHandler(method);
        try {
            ops = logRecordOperationSource.computeLogRecordOperate(method, targetClass);
            if(enableLogRecordProperties.isLogOpenFlag()){
                log.info(LOG_PREFIX + "@LogRecord params：ops{}", ops);
            }
            logRecordExtendHandler.beforeHandler(invocation, extInfo);
            List<String> spElTemplates = getBeforeExecuteFunctionTemplate(ops);
            if(enableLogRecordProperties.isLogOpenFlag()){
                log.info(LOG_PREFIX + "@LogRecord before process spElTemplates：templates{}", spElTemplates);
            }
            funcNameAndReturnMap = processBeforeExecuteFunctionTemplate(spElTemplates, targetClass, method, args);
            if(enableLogRecordProperties.isLogOpenFlag()){
                log.info(LOG_PREFIX + "@LogRecord before process spElTemplates result：resultMap:{}", funcNameAndReturnMap);
            }
        } catch (Exception e) {
            logRecordErrorHandler.handlerError(LogRecordErrorEnum.PARSER_ERROR, invocation, e);
            if(enableLogRecordProperties.isLogOpenFlag()){
                log.error(LOG_PREFIX +"log record parse before function fail invocation:{}. exception:",invocation,  e);
            }
        }

        try {
            ret = invocation.proceed();
            methodExecuteResult.executeSuccess(ret);
        } catch (Exception e) {
            if(enableLogRecordProperties.isLogOpenFlag()){
                log.error(LOG_PREFIX +"target method proceed fail invocation:{}. exception:",invocation,  e);
            }
            methodExecuteResult.executeFail(e, e.getMessage());
        }

        try {
            if (Objects.nonNull(ops)) {
                logRecordExtendHandler.afterHandler(invocation, extInfo);
                recordExecute(methodExecuteResult, funcNameAndReturnMap, ops, extInfo);
            }
        } catch (Exception e) {
            logRecordErrorHandler.handlerError(LogRecordErrorEnum.SAVE_ERROR, invocation, e);
            if (enableLogRecordProperties.isLogOpenFlag()){
                log.error(LOG_PREFIX +"log record save fail invocation:{}. exception:",invocation, e);
            }
        }

        if (Objects.nonNull(methodExecuteResult.getThrowable())) {
            throw methodExecuteResult.getThrowable();
        }
        return ret;
    }

    private ILogRecordExtendHandler getLogRecordExtendHandler(Method method){
        LogRecord annotation = AnnotationUtils.getAnnotation(method, LogRecord.class);
        if (annotation == null) {
            throw new LogRecordException("get @LogRecord annotation fail");
        }
        Class<? extends ILogRecordExtendHandler> extendClazz = annotation.extendClass();
        return extendClazz == null ? applicationContext.getBean(DefaultLogRecordExtendHandler.class) : applicationContext.getBean(extendClazz);
    }

    private List<String> getBeforeExecuteFunctionTemplate(LogRecordOps ops) {
        return getSpElTemplate(ops, ops.getSuccessLog());
    }

    private Map<String, String> processBeforeExecuteFunctionTemplate(List<String> spElTemplates, Class<?> targetClass, Method method, Object[] args) {
        Map<String, String> funcNameAndReturnValueMap = new HashMap<>();
        EvaluationContext evaluationContext = logRecordValueParser.createEvaluationContext(method, args, targetClass, null, null);
        for (String template : spElTemplates) {
            if (template.contains("{")) {
                Matcher matcher = pattern.matcher(template);
                while (matcher.find()) {
                    String expression = matcher.group(2);
                    if (expression.contains("#_ret") || expression.contains("#errorMsg")) {
                        continue;
                    }
                    AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
                    String funcName = matcher.group(1);
                    if (logFunctionExecute.isBeforeFunction(funcName)) {
                        Object value = logRecordValueParser.getExpression(expression, annotatedElementKey, evaluationContext);
                        String funcReturnValue = logFunctionExecute.getFunctionReturnValue(null, value, expression, funcName);
                        String funcCallInstanceKey = logFunctionExecute.getFunctionCallInstanceKey(funcName, expression);
                        funcNameAndReturnValueMap.put(funcCallInstanceKey, funcReturnValue);
                    }
                }
            }

        }
        return funcNameAndReturnValueMap;
    }

    private void recordExecute(MethodExecuteResult executeResult, Map<String, String> funcNameAndReturnMap, LogRecordOps ops, Map<String, Object> extInfo) {
        Runnable runnable = () -> {
            try {
                if (!isRecord(executeResult, funcNameAndReturnMap, ops)) {
                    return;
                }
                if (executeResult.isSuccess()) {
                    successLogRecord(executeResult, funcNameAndReturnMap, ops, extInfo);
                } else {
                    failLogRecord(executeResult, funcNameAndReturnMap, ops, extInfo);
                }
            } catch (Exception e){
                logRecordErrorHandler.handlerError(LogRecordErrorEnum.SAVE_ERROR, executeResult.getInvocation(), e);
                if(enableLogRecordProperties.isLogOpenFlag()){
                    log.error(LOG_PREFIX +"save log fail invocation:{}. exception:",executeResult.getInvocation(),  e);
                }
            } finally {
                // 清理threadLocal
                LogRecordContext.clear();
            }
        };

        if (ops.isAsync()) {
            ttlLogRecordExecutor.execute(TtlRunnable.get(runnable));
        } else {
            runnable.run();
        }
    }

    private List<String> getSpElTemplate(LogRecordOps ops, String... actions) {
        List<String> templates = new ArrayList<>();
        templates.add(ops.getOperateTypeCode());
        templates.add(ops.getBizId());
        templates.add(ops.getBizTypeCode());
        templates.add(ops.getOperatorId());
        templates.add(ops.getOperateTypeCode());
        templates.add(ops.getCondition());
        templates.add(ops.getSuccessCondition());
        templates.addAll(Arrays.asList(actions));
        return templates;
    }

    private boolean isRecord(MethodExecuteResult executeResult, Map<String, String> funcNameAndReturnMap, LogRecordOps ops) {
        if (!StringUtils.isEmpty(ops.getCondition())) {
            String condition = singleProcessTemplate(executeResult, ops.getCondition(), funcNameAndReturnMap);
            return StringUtils.endsWithIgnoreCase(condition, "true");
        }
        return false;
    }

    private void successLogRecord(MethodExecuteResult executeResult, Map<String, String> funcNameAndReturnMap, LogRecordOps ops, Map<String, Object> extInfo) throws Exception {
        String template = "";
        boolean flag = true;
        if (!StringUtils.isEmpty(ops.getSuccessCondition())) {
            String condition = singleProcessTemplate(executeResult, ops.getSuccessCondition(), funcNameAndReturnMap);
            if (StringUtils.endsWithIgnoreCase(condition, "true")) {
                template = ops.getSuccessLog();
            } else {
                template = ops.getFailLog();
                flag = false;
            }
        } else {
            template = ops.getSuccessLog();
        }
        if (StringUtils.isEmpty(template)) return;
        saveLog(executeResult.getMethod(), !flag, ops, template, executeResult, funcNameAndReturnMap, extInfo);
    }

    private void failLogRecord(MethodExecuteResult executeResult, Map<String, String> funcNameAndReturnMap, LogRecordOps ops, Map<String, Object> extInfo) throws Exception {
        if (StringUtils.isEmpty(ops.getFailLog())) {
            return;
        }
        String template = ops.getFailLog();
        saveLog(executeResult.getMethod(), true, ops, template, executeResult, funcNameAndReturnMap, extInfo);
    }

    public String singleProcessTemplate(MethodExecuteResult methodExecuteResult, String template, Map<String, String> funcNameAndReturnMap) {
        Map<String, String> valueMap = processTemplate(methodExecuteResult, Collections.singletonList(template), funcNameAndReturnMap);
        return valueMap.get(template);
    }

    private Map<String, String> processTemplate(MethodExecuteResult executeResult, List<String> templates, Map<String, String> funcNameAndReturnMap) {
        Map<String, String> expressionValues = new HashMap<>();
        EvaluationContext evaluationContext = logRecordValueParser.createEvaluationContext(executeResult.getMethod(), executeResult.getArgs(), executeResult.getTargetClass(), executeResult.getResult(), executeResult.getErrorMsg());
        for (String expressionTemplate : templates) {
            if (expressionTemplate.contains("{")) {
                Matcher matcher = pattern.matcher(expressionTemplate);
                StringBuffer parsedStr = new StringBuffer();
                AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(executeResult.getMethod(), executeResult.getTargetClass());
                boolean flag = true;
                while (matcher.find()) {
                    String expression = matcher.group(2);
                    String funcName = matcher.group(1);
                    Object value = logRecordValueParser.getExpression(expression, annotatedElementKey, evaluationContext);
                    expression = logFunctionExecute.getFunctionReturnValue(funcNameAndReturnMap, value, expression, funcName);
                    if (expression != null && Objects.equals(expression, "")) {
                        flag = false;
                    }
                    matcher.appendReplacement(parsedStr, Matcher.quoteReplacement(expression == null ? "" : expression));
                }
                matcher.appendTail(parsedStr);
                expressionValues.put(expressionTemplate, flag ? parsedStr.toString() : expressionTemplate);
            } else {
                expressionValues.put(expressionTemplate, expressionTemplate);
            }
        }
        return expressionValues;
    }

    private String getOperateIdOrPutTemplate(LogRecordOps ops, List<String> spElTemplates) {
        String operatorId = "";
        if (StringUtils.isEmpty(ops.getOperatorId())) {
            operatorId = Optional.ofNullable(operatorGetService.getOperator()).map(LogRecordOperator::getOperatorId).orElse("");
            if (StringUtils.isEmpty(operatorId)) {
                throw new LogRecordException("operator is null");
            }
        } else {
            spElTemplates.add(ops.getOperatorId());
        }
        return operatorId;
    }

    private void saveLog(Method method, boolean flag, LogRecordOps ops, String template, MethodExecuteResult executeResult, Map<String, String> funcNameAndReturnMap,  Map<String, Object> extInfo) throws Exception {
        List<String> spElTemplates = getSpElTemplate(ops, template);
        if(enableLogRecordProperties.isLogOpenFlag()){
            log.info(LOG_PREFIX + "@LogRecord after process spElTemplates：templates{}", spElTemplates);
        }
        String operatorId = getOperateIdOrPutTemplate(ops, spElTemplates);
        if(enableLogRecordProperties.isLogOpenFlag()){
            log.info(LOG_PREFIX + "@LogRecord get Operator Id Id:{}", operatorId);
        }
        Map<String, String> expressionValueMap = processTemplate(executeResult, spElTemplates, funcNameAndReturnMap);
        if(enableLogRecordProperties.isLogOpenFlag()){
            log.info(LOG_PREFIX + "@LogRecord after process expressionValueMap：expressionValueMap{}", expressionValueMap);
        }
        if (StringUtils.isEmpty(expressionValueMap.get(template))) {
            return;
        }
        LogRecordDto logRecordDto = logRecordDtoFactory.getInstance(LogRecordDto.class);
        logRecordDto.setId(logRecordIdGetService.getLogUniqueId())
                .setSystemCode(enableLogRecordProperties.getSystemCode())
                .setContent(expressionValueMap.get(template))
                .setBizId(expressionValueMap.get(ops.getBizId()))
                .setBizTypeCode(expressionValueMap.get(ops.getBizTypeCode()))
                .setOperateTypeCode(expressionValueMap.get(ops.getOperateTypeCode()))
                .setOperatorId(StringUtils.isEmpty(operatorId) ? expressionValueMap.get(ops.getOperatorId()) : operatorId)
                .setOperateTime(new Date())
                .setExecuteMethod(method.getDeclaringClass() + method.getName())
                .setExtInfo(extInfo);

        logRecordService.record(logRecordDto);
    }


}
