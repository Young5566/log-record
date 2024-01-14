package com.young.log.annotation;

import com.young.log.core.handler.ILogRecordExtendHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Young
 * @description: 方法日志记录注释
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogRecord {

    String successLog();

    String failLog() default "";

    String bizId();

    String bizTypeCode() default "";

    String operatorId() default "";

    String operateTypeCode() default "";

    String condition() default "";

    String successCondition() default "";

    Class<? extends ILogRecordExtendHandler> extendClass();

    boolean isAsync() default true;

}
