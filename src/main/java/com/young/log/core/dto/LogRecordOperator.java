package com.young.log.core.dto;

/**
 * @author: Young
 * @description: 操作人员实体类
 **/
public class LogRecordOperator {

    private String operatorId;

    private String operatorName;

    public String getOperatorId() {
        return operatorId;
    }

    public LogRecordOperator setOperatorId(String operatorId) {
        this.operatorId = operatorId;
        return this;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public LogRecordOperator setOperatorName(String operatorName) {
        this.operatorName = operatorName;
        return this;
    }
}
