package com.young.log.core;

import com.young.log.core.dto.LogRecordOperator;

/**
 * @author: Young
 * @description: 获取操作信息
 **/
public class DefaultOperatorGetService implements IOperatorGetService{


    @Override
    public LogRecordOperator getOperator() {
        return new LogRecordOperator();
    }
}
