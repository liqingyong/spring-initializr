package com.spring.initializr.support;

import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * @ClassName ReProjectContributorConfiguration
 * @Description TODO 修改资源引入
 * @Author lqy
 * @Date 2023/2/3 12:03
 * @Version 1.0
 **/
@ProjectGenerationConfiguration
public class ReProjectContributorConfiguration {

    /**
     * @author lqy
     * @description TODO 配置文件修改并加入项目
     * @date 2023/2/3 18:25
     * @param projectDescription:
     * @return : com.spring.initializr.support.TemplateConfigFileProjectContributor
     **/
    @Bean
    @Autowired(required = false)
    public TemplateConfigFileProjectContributor templateConfigFileProjectContributor(ProjectDescription projectDescription) {
        return new TemplateConfigFileProjectContributor(projectDescription);
    }

    /**
     * @author lqy
     * @description TODO 代码修改并加入项目
     * @date 2023/2/6 10:00
     * @param projectDescription:
     * @return : com.spring.initializr.support.TemplateSourceCodeProjectContributor
     **/
    @Bean
    @Autowired(required = false)
    public TemplateSourceCodeProjectContributor templateSourceCodeProjectContributor(ProjectDescription projectDescription) {
        return new TemplateSourceCodeProjectContributor(projectDescription);
    }
}
