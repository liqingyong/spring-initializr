package com.spring.initializr.customizer;

import com.spring.initializr.consts.SysConst;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionCustomizer;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.metadata.support.MetadataBuildItemMapper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author lqy
 * @description TODO
 * @date 2023/2/3 默认加入工程的内容
 * @return : null
 **/
public class DefaultDependenciesProjectDescriptionCustomizer implements ProjectDescriptionCustomizer {
    private final Set<String> requiredDependenciesId;
    private final InitializrMetadataProvider metadataProvider;

    public DefaultDependenciesProjectDescriptionCustomizer(InitializrMetadataProvider metadataProvider) {
        this.metadataProvider = metadataProvider;
        // 默认添加的依赖
        requiredDependenciesId = new HashSet<>();
        //requiredDependenciesId.add("web");
        //requiredDependenciesId.add("lombok");
    }

    @Override
    public void customize(MutableProjectDescription description) {
        Map<String, Dependency> dependencies = description.getRequestedDependencies();
        Set<String> ids = dependencies.keySet();
        Set<String> diff = diff(getDefaultDescription(dependencies), ids);
        // 添加依赖
        InitializrMetadata metadata = metadataProvider.get();
        diff.forEach(id -> {
            io.spring.initializr.metadata.Dependency dependency = metadata.getDependencies().get(id);
            description.addDependency(id, MetadataBuildItemMapper.toDependency(dependency));
        });
    }

    /**
     * 计算sources和targets的差集：存在于sources中，但不在targets中。
     */
    private Set<String> diff(Set<String> sources, Set<String> targets) {
        Set<String> diff = new HashSet<>();
        for (String source : sources) {
            if (!targets.contains(source)) {
                diff.add(source);
            }
        }
        return diff;
    }


    private Set<String> getDefaultDescription(Map<String, Dependency> description) {
        for (String k : description.keySet()) {
            if (k.equals("mybatis"))
                return mybatis(k);
            else if (k.equals("data-jpa"))
                return hibernate("hibernate");
        }
        return mybatis("mybatis");
    }

    private Set<String> mybatis(String k) {
        requiredDependenciesId.add("web");
        requiredDependenciesId.add("lombok");
        requiredDependenciesId.add("knife4j");
        requiredDependenciesId.add("actuator");
        requiredDependenciesId.add("freemarker");
        requiredDependenciesId.add("mybatis-plus-generator");
        SysConst.rootConfResource = String.format(SysConst.rootConfResource,k);
        SysConst.rootCodeResource = String.format(SysConst.rootCodeResource,k);
        return requiredDependenciesId;
    }

    private Set<String> hibernate(String k) {
        requiredDependenciesId.add("web");
        requiredDependenciesId.add("lombok");
        SysConst.rootConfResource = String.format(SysConst.rootConfResource,k);
        SysConst.rootCodeResource = String.format(SysConst.rootCodeResource,k);
        return requiredDependenciesId;
    }
}