package com.spring.initializr.support;

import com.spring.initializr.consts.SysConst;
import com.spring.initializr.utils.DescriptionUtil;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lqy
 * @description TODO 将resources下的代码替换并增加到项目
 * @date 2023/2/3 18:27
 **/
public class TemplateSourceCodeProjectContributor implements ProjectContributor {


    private final ProjectDescription description;
    private final PathMatchingResourcePatternResolver resolver;

    public TemplateSourceCodeProjectContributor(ProjectDescription description) {
        this.description = description;
        this.resolver = new PathMatchingResourcePatternResolver();
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }


/*
        Path path = Paths.get(root.getURI());
        Files.list(path).forEach(p->{
            System.err.println(p.getFileName());
        });

        */
    /**
     * @author lqy
     * @description TODO 替换文件
     * @date 2023/2/8 18:06
     * @param projectRoot:
     * @return : void
     **/
    @Override
    public void contribute(Path projectRoot) throws IOException {
        String basePackagePath = description.getPackageName().replace('.', File.separatorChar);
        Resource root = this.resolver.getResource(SysConst.rootCodeResource);
        if (!root.exists()) {
            findLocalFile(SysConst.rootCodeResource,projectRoot,basePackagePath);
            return;
        }
        Resource[] resources = this.resolver.getResources(SysConst.rootCodeResource + "/**");
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                String filename = extractFileName(root.getURI(), resource.getURI());
                Path output = projectRoot.resolve(addParentPath(basePackagePath, filename));
                Files.createDirectories(output.getParent());
                Files.createFile(output);
                writeJavaSourceCodeFile(resource.getInputStream(), Files.newOutputStream(output));
            }else {
                //创建空白目录
                String filename = extractFileName(root.getURI(), resource.getURI());
                Path output = projectRoot.resolve(addParentPath(basePackagePath, filename));
                Files.createDirectories(output);
            }
        }
    }

    /**
     * @author lqy
     * @description TODO 查找本地文件
     * @date 2023/2/9 18:38
     * @param path:
     * @param projectRoot:
     * @return : void
     **/
    private void findLocalFile(String path,Path projectRoot,String basePackagePath) throws IOException {
        Path path_ = Paths.get(path);
        if (!Files.exists(path_))
            return;
        String curr_dir = path_.toString().replace("\\","/").replace(SysConst.rootCodeResource.replace("\\","/"),"") ;
        Path output = projectRoot.resolve(addParentPath(basePackagePath,curr_dir));
        if (Files.isDirectory(path_)) {
            //创建空白目录
            Files.createDirectories(output);
            Files.list(path_).forEach(f-> {
                try {
                    findLocalFile(f.toString(),projectRoot,basePackagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }else {
            Files.createDirectories(output.getParent());
            Files.createFile(output);
            writeJavaSourceCodeFile(Files.newInputStream(path_), Files.newOutputStream(output));
        }
    }
    /**
     * 获取resource表示的文件相对路径
     */
    private String extractFileName(URI root, URI resource) {
        String candidate = resource.toString().substring(root.toString().length());
        return StringUtils.trimLeadingCharacter(candidate, '/');
    }

    /**
     * 添加父路径：src/main/java/{basePackagePath}
     */
    private String addParentPath(String basePackagePath, String fileName) {
        return SysConst.codeDirPrefix + File.separatorChar + basePackagePath + File.separatorChar + fileName;
    }

    public void writeJavaSourceCodeFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
            Pattern compile = Pattern.compile(SysConst.templateRex);
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                Matcher matcher = compile.matcher(line);
                while (matcher.find()) {
                    //替换数据
                    line = line.replace(matcher.group(), Objects.requireNonNull(DescriptionUtil.getDescriptionVal(matcher.group(1), this.description)));
                }
                writer.write(line);
                writer.write("\n");
                matcher.reset();
            }
        }
    }

}
