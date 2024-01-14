package com.young.log.Enum;

/**
 * @author: Young
 * @description: 日志记录异常枚举
 **/
public enum LogRecordErrorEnum {
    PARSER_ERROR("parser_error", "解析异常"),
    SAVE_ERROR("save_error", "记录异常");

    private final String code;

    private final String message;

    LogRecordErrorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
