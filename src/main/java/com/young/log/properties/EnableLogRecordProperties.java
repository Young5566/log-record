package com.young.log.properties;

/**
 * @author: Young
 * @description: EnableLogRecord属性实体
 **/
public class EnableLogRecordProperties {
    private String systemCode;

    private int order;

    private boolean logOpenFlag;

    public String getSystemCode() {
        return systemCode;
    }

    public EnableLogRecordProperties setSystemCode(String systemCode) {
        this.systemCode = systemCode;
        return this;
    }

    public int getOrder() {
        return order;
    }

    public EnableLogRecordProperties setOrder(int order) {
        this.order = order;
        return this;
    }

    public static EnableLogRecordProperties builder(){
        return new EnableLogRecordProperties();
    }

    public boolean isLogOpenFlag() {
        return logOpenFlag;
    }

    public EnableLogRecordProperties setLogOpenFlag(boolean logOpenFlag) {
        this.logOpenFlag = logOpenFlag;
        return this;
    }
}
