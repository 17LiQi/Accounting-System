package com.as.server.config;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS) // 启用枚举不区分大小写
                .addModules(new JavaTimeModule()) // 注册 JavaTimeModule，用于处理日期和时间
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // 禁用默认的将日期时间序列化为时间戳
                .defaultTimeZone(TimeZone.getTimeZone("UTC"))
                .build();
    }
}