package com.xie.spring.boot;

import com.xie.auto.config.Client;
import com.xie.auto.config.MqConfigProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@RestController
public class AppBoot implements ApplicationContextAware{
    ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(AppBoot.class, args);
    }

    @Autowired(required = false)
    private Client client;

    @Autowired
    private MqConfigProperties configProperties;

    @RequestMapping("/")
    String helloword(){
        client.start();
        return "hello word !!!";
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
