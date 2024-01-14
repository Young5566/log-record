package com.young.log.annotation;

import com.young.log.config.LogRecordAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * @author: Young
 * @description: 开启日志记录自动注解
 **/
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({LogRecordAutoConfiguration.class})
public @interface EnableLogRecord {

    String systemCode() default "";

    int order() default Ordered.LOWEST_PRECEDENCE;

    boolean logOpenFlag() default false;
}
