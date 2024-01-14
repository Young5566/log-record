package com.young.log.core.record;

import com.alibaba.fastjson.JSON;
import com.young.log.core.dto.LogRecordDto;
import com.young.log.properties.RocketMqProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * @author: Young
 * @description: 通过MQ方式记录操作日志
 **/

@Slf4j
@EnableConfigurationProperties({RocketMqProperties.class})
@ConditionalOnProperty(name = "log-record.data-pipeline", havingValue = "rocket_mq")
@Component
public class LogRecordByRocketMq implements ILogRecordService{

    @Resource(name = "rocketMqProducer")
    private DefaultMQProducer mqProducer;

    @Resource
    private RocketMqProperties rocketMqProperties;

    @Override
    public boolean record(LogRecordDto dto) {
        try {
            Message msg = new Message(rocketMqProperties.getTopic(), rocketMqProperties.getTag(), JSON.toJSONString(dto).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = mqProducer.send(msg);
            log.info("log record Rocket MQ send LogDto dto:{}, result {}", dto, sendResult);
            return true;
        } catch (Exception e) {
            log.error("rocketMq send log error e", e);
        }
        return false;
    }
}
