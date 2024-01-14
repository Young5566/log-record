package com.young.log.core.record;

import com.young.log.core.dto.LogRecordDto;

/**
 * @author: Young
 * @description: 记录日志接口，实现此接口可自定义具体日志操作
 **/
public interface ILogRecordService {

    boolean record(LogRecordDto dto);

}
