package com.young.log.config;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.young.log.annotation.EnableLogRecord;
import com.young.log.core.DefaultLogRecordIdGetService;
import com.young.log.core.DefaultOperatorGetService;
import com.young.log.core.ILogRecordIdGetService;
import com.young.log.core.IOperatorGetService;
import com.young.log.core.expression.DefaultFunctionService;
import com.young.log.core.expression.DefaultParseFunction;
import com.young.log.core.expression.IFunctionService;
import com.young.log.core.expression.IParseFunction;
import com.young.log.core.factory.LogRecordDtoFactory;
import com.young.log.core.factory.LogRecordExecuteThreadFactory;
import com.young.log.core.factory.ParseFunctionFactory;
import com.young.log.core.handler.*;
import com.young.log.core.interceptor.LogRecordInterceptor;
import com.young.log.core.record.DefaultLogRecord;
import com.young.log.core.record.ILogRecordService;
import com.young.log.properties.EnableLogRecordProperties;
import com.young.log.properties.LogRecordThreadPoolProperties;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Executor;

import static com.young.log.constants.LogRecordConstants.INTERCEPTOR_EXPRESSION;
import static com.young.log.constants.LogRecordConstants.ORDER;

/**
 * @author: Young
 * @description: 日志记录配置
 **/

@Configuration
@ComponentScan("com.young.log")
@EnableConfigurationProperties({LogRecordThreadPoolProperties.class})
public class LogRecordAutoConfiguration implements ImportAware {
    protected AnnotationAttributes annotationAttributes;

    @Resource
    private LogRecordThreadPoolProperties threadPoolProperties;

    @Bean
    public LogRecordDtoFactory logRecordDtoFactory(){
        return new LogRecordDtoFactory();
    }

    @Bean(name = "logRecordExecutor")
    @ConditionalOnMissingBean(name = "logRecordExecutor")
    public Executor LogRecordExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
        executor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
        executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        executor.setThreadFactory(new LogRecordExecuteThreadFactory());
        return executor;
    }

    @Bean(name = "ttlLogRecordExecutor")
    @DependsOn("logRecordExecutor")
    public Executor TtlLogRecordExecutor(Executor logRecordExecutor){
        return TtlExecutors.getTtlExecutor(logRecordExecutor);
    }

    @Bean(name = "logRecordErrorHandler")
    @ConditionalOnMissingBean(value = ILogRecordErrorHandler.class)
    public ILogRecordErrorHandler logRecordErrorHandler(EnableLogRecordProperties enableLogRecordProperties){
        return new DefaultLogRecordErrorHandler(enableLogRecordProperties);
    }

    @Bean(name = "logRecordExtendHandler")
    @ConditionalOnMissingBean(value = ILogRecordExtendHandler.class)
    public ILogRecordExtendHandler logRecordExtendHandler(EnableLogRecordProperties enableLogRecordProperties){
        return new DefaultLogRecordExtendHandler(enableLogRecordProperties);
    }

    @Bean(name = "logRecordAdvisor")
    public AspectJExpressionPointcutAdvisor aspectJExpressionPointcutAdvisor(LogRecordHandler logRecordHandler){
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setAdvice(new LogRecordInterceptor(logRecordHandler));
        advisor.setExpression(INTERCEPTOR_EXPRESSION);
        advisor.setOrder(annotationAttributes.getNumber(ORDER));
        return advisor;
    }

    @Bean
    @ConditionalOnMissingBean(IFunctionService.class)
    public IFunctionService functionService(ParseFunctionFactory parseFunctionFactory){
        return new DefaultFunctionService(parseFunctionFactory);
    }

    @Bean
    public ParseFunctionFactory parseFunctionFactory(List<IParseFunction> parseFunctions){
        return new ParseFunctionFactory(parseFunctions);
    }

    @Bean
    @ConditionalOnMissingBean(IParseFunction.class)
    public DefaultParseFunction parseFunction(){
        return new DefaultParseFunction();
    }

    @Bean
    public EnableLogRecordProperties enableLogRecordProperties(){
        EnableLogRecordProperties propesties = EnableLogRecordProperties.builder()
                .setSystemCode((String) annotationAttributes.get("systemCode"))
                .setOrder((Integer) annotationAttributes.get("order"))
                .setLogOpenFlag((Boolean) annotationAttributes.get("logOpenFlag"))
                ;
        return propesties;
    }

    @Bean
    @ConditionalOnMissingBean(ILogRecordService.class)
    public DefaultLogRecord logRecordRecord(EnableLogRecordProperties properties){
        return new DefaultLogRecord(properties);
    }

    @Bean
    @ConditionalOnMissingBean(IOperatorGetService.class)
    public DefaultOperatorGetService operatorGetService(){
        return new DefaultOperatorGetService();
    }

    @Bean
    @ConditionalOnMissingBean(ILogRecordIdGetService.class)
    public DefaultLogRecordIdGetService logRecordIdGetService(){
        return new DefaultLogRecordIdGetService();
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        annotationAttributes = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableLogRecord.class.getName() , false));
    }
}
