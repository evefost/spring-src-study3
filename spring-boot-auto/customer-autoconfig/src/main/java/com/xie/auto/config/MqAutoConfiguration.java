package com.xie.auto.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({MqConfigProperties.class})
public class MqAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "customer.rocketmq.enabled", matchIfMissing = true)
    Client getRocketMqClient(){
        return new RocketMqClient();
    }

    @Bean
    @ConditionalOnProperty(value = "customer.activemq.enabled")
    Client getActiveMqClient(){
        return new ActiveMqClient();
    }
}
