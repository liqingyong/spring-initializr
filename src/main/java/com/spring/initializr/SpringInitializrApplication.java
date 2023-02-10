package com.spring.initializr;

import com.spring.initializr.configurer.CustomInitializrConfiguration;
import com.spring.initializr.configurer.CustomInitializrMetadataUpdateStrategyConfiguration;
import com.spring.initializr.customizer.ProjectDescriptionCustomizerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableCaching
@SpringBootApplication
@ComponentScan(excludeFilters =
        {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.spring.initializr.support.*"),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.spring.initializr.contributor.*")
        })
@Import({CustomInitializrConfiguration.class, ProjectDescriptionCustomizerConfiguration.class, CustomInitializrMetadataUpdateStrategyConfiguration.class})
public class SpringInitializrApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringInitializrApplication.class, args);
    }

}
