package com.young.log.core;

import com.young.log.core.dto.LogRecordOperator;

/**
 * @author: Young
 * @description: 获取操作人员Id
 **/
@FunctionalInterface
public interface IOperatorGetService {

    LogRecordOperator getOperator();

}
