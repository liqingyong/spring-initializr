package com.spring.initializr.customizer;

import io.spring.initializr.metadata.InitializrMetadataProvider;
import org.springframework.context.annotation.Bean;

/**
 * @ClassName ProjectDescriptionCustomizerConfiguration
 * @Description TODO 自定义bean
 * @Author lqy
 * @Date 2023/2/3 11:34
 * @Version 1.0
 **/
public class ProjectDescriptionCustomizerConfiguration {
    @Bean
    public DefaultDependenciesProjectDescriptionCustomizer appendDependenciesProjectDescriptionCustomizer(InitializrMetadataProvider metadataProvider) {
        return new DefaultDependenciesProjectDescriptionCustomizer(metadataProvider);
    }
}
