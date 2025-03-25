package com.example.config;

import com.redis.sessions.indexing.IndexedField;
import com.redis.sessions.indexing.RedisIndexConfiguration;
import com.redis.sessions.spring.config.annotation.web.http.EnableRedisSessions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(name = "spring.session.store-type", havingValue = "redisSessions")
@Configuration
@EnableRedisSessions
public class RedisConfig {
    @Bean
    public RedisIndexConfiguration redisIndexConfiguration(){
        return RedisIndexConfiguration.builder()
                .withField(IndexedField.numeric("lastAccessedTime").javaType(Integer.class).build())
                .withField(IndexedField.numeric("createdAt").javaType(Integer.class).build()).build();
    }
}
