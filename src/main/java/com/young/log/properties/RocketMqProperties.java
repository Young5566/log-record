package com.young.log.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: Young
 * @description: rocketMq配置文件
 **/
@ConfigurationProperties(prefix = "log-record.rocket-mq")
public class RocketMqProperties {
    private String topic = "log_record";
    private String tag = "";

    private String nameServerAddr = "localhost:9876";

    private String groupName = "logRecord";

    private int maxMessageSize = 4000000;

    private int sendMsgTimeOut = 3000;

    private int retryTimesWhenSendFailed = 2;

    public String getTopic() {
        return topic;
    }

    public RocketMqProperties setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public RocketMqProperties setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getNameServerAddr() {
        return nameServerAddr;
    }

    public RocketMqProperties setNameServerAddr(String nameServerAddr) {
        this.nameServerAddr = nameServerAddr;
        return this;
    }

    public String getGroupName() {
        return groupName;
    }

    public RocketMqProperties setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public int getMaxMessageSize() {
        return maxMessageSize;
    }

    public RocketMqProperties setMaxMessageSize(int maxMessageSize) {
        this.maxMessageSize = maxMessageSize;
        return this;
    }

    public int getSendMsgTimeOut() {
        return sendMsgTimeOut;
    }

    public RocketMqProperties setSendMsgTimeOut(int sendMsgTimeOut) {
        this.sendMsgTimeOut = sendMsgTimeOut;
        return this;
    }

    public int getRetryTimesWhenSendFailed() {
        return retryTimesWhenSendFailed;
    }

    public RocketMqProperties setRetryTimesWhenSendFailed(int retryTimesWhenSendFailed) {
        this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
        return this;
    }
}
