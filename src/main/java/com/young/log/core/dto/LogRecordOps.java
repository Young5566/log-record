package com.young.log.core.dto;

import com.young.log.core.handler.ILogRecordExtendHandler;

/**
 * @author: Young
 * @description: LogRecord注释参数
 **/
public class LogRecordOps {

    private String successLog;

    private String failLog;

    private String bizId;

    private String bizTypeCode;

    private String operatorId;

    private String operateTypeCode;

    private String condition;

    private String successCondition;

    private Class<? extends ILogRecordExtendHandler> extendClass;

    private boolean isAsync;

    public String getSuccessLog() {
        return successLog;
    }

    public LogRecordOps setSuccessLog(String successLog) {
        this.successLog = successLog;
        return this;
    }

    public String getFailLog() {
        return failLog;
    }

    public LogRecordOps setFailLog(String failLog) {
        this.failLog = failLog;
        return this;
    }

    public String getBizId() {
        return bizId;
    }

    public LogRecordOps setBizId(String bizId) {
        this.bizId = bizId;
        return this;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public LogRecordOps setOperatorId(String operatorId) {
        this.operatorId = operatorId;
        return this;
    }

    public String getOperateTypeCode() {
        return operateTypeCode;
    }

    public LogRecordOps setOperateTypeCode(String operateTypeCode) {
        this.operateTypeCode = operateTypeCode;
        return this;
    }

    public String getCondition() {
        return condition;
    }

    public LogRecordOps setCondition(String condition) {
        this.condition = condition;
        return this;
    }

    public boolean isAsync() {
        return isAsync;
    }

    public LogRecordOps setAsync(boolean async) {
        isAsync = async;
        return this;
    }

    public String getBizTypeCode() {
        return bizTypeCode;
    }

    public LogRecordOps setBizTypeCode(String bizTypeCode) {
        this.bizTypeCode = bizTypeCode;
        return this;
    }

    public String getSuccessCondition() {
        return successCondition;
    }

    public LogRecordOps setSuccessCondition(String successCondition) {
        this.successCondition = successCondition;
        return this;
    }

    public Class<? extends ILogRecordExtendHandler> getExtendClass() {
        return extendClass;
    }

    public LogRecordOps setExtendClass(Class<? extends ILogRecordExtendHandler> extendClass) {
        this.extendClass = extendClass;
        return this;
    }

    public LogRecordOps build(){
        return this;
    }

    @Override
    public String toString() {
        return "LogRecordOps{" +
                "successLog='" + successLog + '\'' +
                ", failLog='" + failLog + '\'' +
                ", bizId='" + bizId + '\'' +
                ", bizTypeCode='" + bizTypeCode + '\'' +
                ", operatorId='" + operatorId + '\'' +
                ", operateTypeCode='" + operateTypeCode + '\'' +
                ", condition='" + condition + '\'' +
                ", successCondition='" + successCondition + '\'' +
                ", extendClass=" + extendClass +
                ", isAsync=" + isAsync +
                '}';
    }
}
