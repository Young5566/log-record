package com.young.log.core.factory;

import com.young.log.core.dto.LogRecordDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Young
 * @description: 日志上下文创建工厂
 **/
@Slf4j
public class LogRecordDtoFactory implements InstanceFactory<LogRecordDto, Class<? extends  LogRecordDto>> {

    /**
     * 存储日志上下文原实例
     */
    private final Map<Class<?>, LogRecordDto> instanceMap = new ConcurrentHashMap<>();

    /**
     * 获取日志上下文实例
     * @param logRecordDtoClazz
     * @return 日志上下文
     * @throws Exception
     */
    @Override
    public LogRecordDto getInstance(@NonNull Class<? extends  LogRecordDto> logRecordDtoClazz) throws Exception {

        LogRecordDto logRecordDto = instanceMap.get(logRecordDtoClazz);

        try {
            if(logRecordDto != null){
                return logRecordDto.getClone();
            }
        } catch (Exception e){
            return logRecordDtoClazz.newInstance();
        }

        synchronized (instanceMap){
            logRecordDto = instanceMap.get(logRecordDtoClazz);
            if(logRecordDto == null){
                logRecordDto = logRecordDtoClazz.newInstance();
                instanceMap.put(logRecordDtoClazz, logRecordDto);
            }
            return logRecordDto.getClone();
        }
    }
}
