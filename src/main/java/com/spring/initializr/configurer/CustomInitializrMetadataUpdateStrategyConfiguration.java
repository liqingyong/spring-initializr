package com.spring.initializr.configurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.initializr.metadata.SpringIoInitializrMetadataUpdateStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName CustomInitializrMetadataUpdateStrategyConfiguration
 * @Description TODO
 * @Author lqy
 * @Date 2023/2/8 10:45
 * @Version 1.0
 **/
@Configuration
public class CustomInitializrMetadataUpdateStrategyConfiguration {

    /**
     * @author lqy
     * @description TODO SpringBoot 版本自动更新
     * @date 2023/2/8 10:51
     * @param restTemplateBuilder:
     * @param objectMapper:
     * @return : com.spring.initializr.metadata.SpringIoInitializrMetadataUpdateStrategy
     **/
    @Bean
    @Autowired(required = false)
    public SpringIoInitializrMetadataUpdateStrategy springIoInitializrMetadataUpdateStrategy(
            RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
        return new SpringIoInitializrMetadataUpdateStrategy(restTemplateBuilder.build(),objectMapper);
    }
}
