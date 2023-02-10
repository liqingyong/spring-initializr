package com.spring.initializr.contributor;

import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * @author lqy
 * @description TODO 替换原生成文件
 * @date 2023/2/3 11:38
 **/
@ProjectGenerationConfiguration
public class ReplaceProjectContributorConfiguration {

    @Bean
    public ReplaceProjectContributorBeanPostProcessor replaceProjectContributorBeanPostProcessor() {
        return new ReplaceProjectContributorBeanPostProcessor();
    }

    /**
     * 将一些ProjectContributor实例替换为NoOpContributor实例：
     * <ul>
     *     <li>替换MavenWrapperContributor，避免生成maven wrapper文件</li>
     *     <li>替换WebFoldersContributor，避免生成src/main/resources/templates和src/main/resources/static目录</li>
     *     <li>替换HelpDocumentProjectContributor，避免生成HELP.md文件</li>
     * </ul>
     */
    private static class ReplaceProjectContributorBeanPostProcessor implements BeanPostProcessor {

        private final List<String> replaceList;
        private final ProjectContributor noOp;

        public ReplaceProjectContributorBeanPostProcessor() {
            replaceList = List.of("mavenWrapperContributor", "webFoldersContributor", "helpDocumentProjectContributor");
            noOp = new NoOpProjectContributor();
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof ProjectContributor && replaceList.contains(beanName)) {
                return noOp;
            } else {
                return bean;
            }
        }
    }

    private static class NoOpProjectContributor implements ProjectContributor {

        @Override
        public void contribute(Path path) throws IOException {

        }

        @Override
        public int getOrder() {
            return ProjectContributor.super.getOrder();
        }
    }
}