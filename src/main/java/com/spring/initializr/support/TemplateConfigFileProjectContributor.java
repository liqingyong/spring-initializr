package com.spring.initializr.support;

import com.spring.initializr.consts.SysConst;
import com.spring.initializr.utils.DescriptionUtil;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * @author lqy
 * 1. 添加以下配置文件至项目的resources目录下：
 * <ul>
 *     <li>application.yaml (copy from application.yam)</li>
 *     <li>application-dev.yaml (copy from application-dev.yam)</li>
 *     <li>application-prod.yaml (copy from application-prod.yam)</li>
 *     <li>ehcache.xml</li>
 *     <li>logback.xml</li>
 * </ul>
 * <p>
 *     2. 替换配置文件中的占位符：${controllerPackage}, ${artifactId}, ${version}
 * </p>
 *
 * <p>
 *     3. 同时，删除已有的application.properties文件；
 * </p>
 * @date 2023/2/3 16:18
 **/
public class TemplateConfigFileProjectContributor implements ProjectContributor {

    private static final Logger log = LoggerFactory.getLogger(TemplateConfigFileProjectContributor.class);

    private final ProjectDescription description;
    private final PathMatchingResourcePatternResolver resolver;

    public TemplateConfigFileProjectContributor(ProjectDescription description) {
        this.description = description;
        this.resolver = new PathMatchingResourcePatternResolver();
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void contribute(Path projectRoot) throws IOException {
        Resource root = this.resolver.getResource(SysConst.rootConfResource);
        // 删除application.properties文件
        File f = getDefaultConfigFile(projectRoot.toFile());
        if (f != null && f.delete()) {
            log.debug("delete file '{}' successfully", SysConst.defaultConfigFile);
        }
        if (!root.exists()) {
            findLocalFile(SysConst.rootConfResource,projectRoot);
            return;
        }
        Resource[] resources = this.resolver.getResources(SysConst.rootConfResource + "/**");
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                String filename = extractFileName(root.getURI(), resource.getURI());
                Path output = projectRoot.resolve(addParentPath(filename));
                Files.createDirectories(output.getParent());
                Files.createFile(output);
                InputStream inputStream = processingConfigFile(resource.getInputStream(), filename);
                FileCopyUtils.copy(inputStream, Files.newOutputStream(output));
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
    private void findLocalFile(String path,Path projectRoot) throws IOException {
        Path path_ = Paths.get(path);
        if (!Files.exists(path_))
            return;
        if (Files.isDirectory(path_)) {
            Files.list(path_).forEach(f-> {
                try {
                    findLocalFile(f.toString(),projectRoot);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }else {
            String replace = path_.toString().replace("\\", "/");
            String replace1 = SysConst.rootConfResource.replace("\\", "/");
            String curr_file = replace.replace(replace1,"");
            Path output = projectRoot.resolve(addParentPath(curr_file));
            Files.createDirectories(output.getParent());
            Files.createFile(output);
            InputStream inputStream = processingConfigFile(Files.newInputStream(path_), path_.getFileName().toString());
            FileCopyUtils.copy(inputStream, Files.newOutputStream(output));
        }
    }

    private String extractFileName(URI root, URI resource) {
        String candidate = resource.toString().substring(root.toString().length());
        String filename = StringUtils.trimLeadingCharacter(candidate, '/');
        if (filename.endsWith("yam")) {
            filename = filename + "l";
        }
        return filename;
    }

    /**
     * 替换配置文件中的占位符
     */
    private InputStream processingConfigFile(InputStream inputStream, String filename) throws IOException {
        if (isProcessing(filename)) {
            return processingConfigFile(inputStream);
        }
        return inputStream;
    }

    /**
     * @author lqy
     * @description TODO 是否需要处理
     * @date 2023/2/9 15:16
     * @param filename:
     * @return : boolean
     **/
    private boolean isProcessing(String filename) {
        final String[] arr = new String[] {"yal","yaml","xml","properties","dockerfile","md"};
        for (String s : arr) {
            if (filename.toLowerCase(Locale.ROOT).endsWith(s))
                return true;
        }
        return false;
    }

    /**
     * 替换spring配置文件中的占位符
     */
    private InputStream processingConfigFile(InputStream inputStream) throws IOException {
        StringJoiner fileContent = new StringJoiner("\n");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        Pattern compile = Pattern.compile(SysConst.templateRex);
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            Matcher matcher = compile.matcher(line);
            while (matcher.find()) {
                //替换数据
                line = line.replace(matcher.group(), Objects.requireNonNull(DescriptionUtil.getDescriptionVal(matcher.group(1), this.description)));
            }
            fileContent.add(line);
            matcher.reset();
        }
        return new ByteArrayInputStream(fileContent.toString().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 添加父路径：src/main/resources
     */
    private String addParentPath(String fileName) {
        return SysConst.resourcesDirPrefix + File.separatorChar + fileName;
    }

    public File getDefaultConfigFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    File target = getDefaultConfigFile(f);
                    if (target != null) {
                        return target;
                    }
                }
            }
        } else if (file.isFile() && Objects.equals(SysConst.defaultConfigFile, file.getName())) {
            return file;
        }
        return null;
    }
}
