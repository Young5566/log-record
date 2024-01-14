package com.young.log.core.record;

import com.young.log.core.dto.LogRecordDto;
import com.young.log.properties.EnableLogRecordProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import static com.young.log.constants.LogRecordConstants.LOG_PREFIX;

/**
 * @author: Young
 * @description: 默认日志记录实现
 **/
@Slf4j
public class DefaultLogRecord implements ILogRecordService{

    private EnableLogRecordProperties enableLogRecordProperties;

    public DefaultLogRecord(EnableLogRecordProperties enableLogRecordProperties) {
        this.enableLogRecordProperties = enableLogRecordProperties;
    }

    @Override
    public boolean record(LogRecordDto dto) {
        if(enableLogRecordProperties.isLogOpenFlag()){
            log.info(LOG_PREFIX + "@LogRecord record log dto: {}", dto);
        }
        return true;
    }
}
