package com.young.log.core.dto;

import java.util.Date;
import java.util.Map;

/**
 * @author: Young
 * @description: 日志实体，一条日志记录
 **/
public class LogRecordDto implements Cloneable{

    private String id;

    private String systemCode;

    private String content;

    private String operateTypeCode;

    private String operatorId;

    private Date operateTime;

    private String bizId;

    private String bizTypeCode;

    private Map<String, Object> request;

    private boolean result;

    private String errorContent;

    private String executeMethod;

    private Map<String, Object> extInfo;

    public String getId() {
        return id;
    }

    public LogRecordDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public LogRecordDto setSystemCode(String systemCode) {
        this.systemCode = systemCode;
        return this;
    }

    public String getContent() {
        return content;
    }

    public LogRecordDto setContent(String content) {
        this.content = content;
        return this;
    }

    public String getOperateTypeCode() {
        return operateTypeCode;
    }

    public LogRecordDto setOperateTypeCode(String operateTypeCode) {
        this.operateTypeCode = operateTypeCode;
        return this;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public LogRecordDto setOperatorId(String operatorId) {
        this.operatorId = operatorId;
        return this;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public LogRecordDto setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
        return this;
    }

    public String getBizId() {
        return bizId;
    }

    public LogRecordDto setBizId(String bizId) {
        this.bizId = bizId;
        return this;
    }

    public String getBizTypeCode() {
        return bizTypeCode;
    }

    public LogRecordDto setBizTypeCode(String bizTypeCode) {
        this.bizTypeCode = bizTypeCode;
        return this;
    }

    public Map<String, Object> getRequest() {
        return request;
    }

    public LogRecordDto setRequest(Map<String, Object> request) {
        this.request = request;
        return this;
    }

    public boolean isResult() {
        return result;
    }

    public LogRecordDto setResult(boolean result) {
        this.result = result;
        return this;
    }

    public String getErrorContent() {
        return errorContent;
    }

    public LogRecordDto setErrorContent(String errorContent) {
        this.errorContent = errorContent;
        return this;
    }

    public String getExecuteMethod() {
        return executeMethod;
    }

    public LogRecordDto setExecuteMethod(String executeMethod) {
        this.executeMethod = executeMethod;
        return this;
    }

    public Map<String, Object> getExtInfo() {
        return extInfo;
    }

    public LogRecordDto setExtInfo(Map<String, Object> extInfo) {
        this.extInfo = extInfo;
        return this;
    }

    public LogRecordDto build(){
        return this;
    }

    public LogRecordDto getClone() throws CloneNotSupportedException {
        return (LogRecordDto) super.clone();
    }

    @Override
    public String toString() {
        return "LogRecordDto{" +
                "id='" + id + '\'' +
                ", systemCode='" + systemCode + '\'' +
                ", content='" + content + '\'' +
                ", operateTypeCode='" + operateTypeCode + '\'' +
                ", operatorId='" + operatorId + '\'' +
                ", operateTime=" + operateTime +
                ", bizId='" + bizId + '\'' +
                ", bizTypeCode='" + bizTypeCode + '\'' +
                ", request=" + request +
                ", result=" + result +
                ", errorContent='" + errorContent + '\'' +
                ", executeMethod='" + executeMethod + '\'' +
                ", extInfo=" + extInfo +
                '}';
    }
}
