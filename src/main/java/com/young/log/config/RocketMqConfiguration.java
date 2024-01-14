package com.young.log.config;

import com.young.log.properties.EnableLogRecordProperties;
import com.young.log.properties.RocketMqProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author: Young
 * @description: Rocket MQ 配置
 **/
@Slf4j
@Configuration
@ConditionalOnProperty(name = "log-record.data-pipeline", havingValue = "rocket_mq")
@EnableConfigurationProperties({RocketMqProperties.class})
public class RocketMqConfiguration {

    @Resource
    private RocketMqProperties rocketMqProperties;

    @Resource
    private EnableLogRecordProperties enableLogRecordProperties;

    @Bean("rocketMqProducer")
    public DefaultMQProducer defaultMQProducer(){
        DefaultMQProducer mqProducer = new DefaultMQProducer(rocketMqProperties.getGroupName());
        mqProducer.setNamesrvAddr(rocketMqProperties.getNameServerAddr());
        mqProducer.setCreateTopicKey(rocketMqProperties.getTopic());
        // 如果需要同一个 jvm 中不同的 producer 往不同的 mq 集群发送消息，需要设置不同的 instanceName
        //producer.setInstanceName(instanceName);
        mqProducer.setMaxMessageSize(rocketMqProperties.getMaxMessageSize());
        mqProducer.setSendMsgTimeout(rocketMqProperties.getSendMsgTimeOut());
        mqProducer.setRetryTimesWhenSendFailed(rocketMqProperties.getRetryTimesWhenSendFailed());
        try {
            mqProducer.start();
            if(enableLogRecordProperties.isLogOpenFlag()){
                log.info("Rocket MQ producer is stared");
            }
        } catch (Exception e){
            if(enableLogRecordProperties.isLogOpenFlag()){
                log.error("Rocket MQ producer start error", e);
            }
        }
        return mqProducer;
    }

}
