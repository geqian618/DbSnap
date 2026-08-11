package com.geqian.dbsnap.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.geqian.dbsnap.properties.AbstractDataSourceProperties;
import com.geqian.dbsnap.serializer.DataSourcePropertiesDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        // 关键：仅针对抽象父类注册，子类不受影响
        module.addDeserializer(AbstractDataSourceProperties.class, new DataSourcePropertiesDeserializer());
        mapper.registerModule(module);
        return mapper;
    }
}