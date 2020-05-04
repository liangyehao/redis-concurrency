package com.liang.config;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liangyehao
 * @version 1.0
 * @date 2020/5/4 18:07
 * @content
 */
@Configuration
public class RedissonConfig {

    @Bean
    public Redisson redisson(){
        //此为单机模式
        Config config = new Config();
        config.useSingleServer().setAddress("http://127.0.0.1:6379").setDatabase(0);
        return (Redisson) Redisson.create(config);
    }
}
