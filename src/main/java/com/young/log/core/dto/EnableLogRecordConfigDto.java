package com.young.log.core.dto;

/**
 * @author: Young
 * @description: EnableLogRecord注释参数实体
 **/
public class EnableLogRecordConfigDto {

    private String systemCode;

    private int order;

    public String getSystemCode() {
        return systemCode;
    }

    public EnableLogRecordConfigDto setSystemCode(String systemCode) {
        this.systemCode = systemCode;
        return this;
    }

    public int getOrder() {
        return order;
    }

    public EnableLogRecordConfigDto setOrder(int order) {
        this.order = order;
        return this;
    }

    public EnableLogRecordConfigDto build(){
        return this;
    }
}
