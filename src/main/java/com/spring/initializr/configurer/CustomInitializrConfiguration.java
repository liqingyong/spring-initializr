package com.spring.initializr.configurer;


import com.spring.initializr.metadata.SpringIoInitializrMetadataUpdateStrategy;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.InitializrMetadataBuilder;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.metadata.InitializrProperties;
import io.spring.initializr.web.support.DefaultInitializrMetadataProvider;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lqy
 * @description TODO 自定义配置导入
 * @date 2023/2/3 11:29
 **/
@Configuration
@EnableConfigurationProperties(CustomInitializrProperties.class)
public class CustomInitializrConfiguration {
    @Resource
    SpringIoInitializrMetadataUpdateStrategy springIoInitializrMetadataUpdateStrategy;

    @Bean
    @Autowired(required = false)
    public InitializrMetadataProvider customInitializrMetadataProvider(InitializrProperties initializrProperties,
                                                                       CustomInitializrProperties customInitializrProperties) {
        InitializrMetadata metadata = InitializrMetadataBuilder
                .fromInitializrProperties(customInitializrProperties.getInitializr())
                .withInitializrProperties(initializrProperties, true)
                .build();
        metadata = springIoInitializrMetadataUpdateStrategy.update(metadata);
        return new DefaultInitializrMetadataProvider(metadata, current -> current);
    }

}

/**
 * @author lqy
 * @description TODO 自定义配置注入
 * @date 2023/2/3 11:30
 **/
@ConfigurationProperties("custom-initializr")
class CustomInitializrProperties {

    @NestedConfigurationProperty
    private InitializrProperties initializr;

    public InitializrProperties getInitializr() {
        return initializr;
    }

    public void setInitializr(InitializrProperties initializr) {
        this.initializr = initializr;
    }
}