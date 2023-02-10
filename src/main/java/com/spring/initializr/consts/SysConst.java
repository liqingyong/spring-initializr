package com.spring.initializr.consts;

import java.io.File;

/**
 * @ClassName SysConst
 * @Description TODO 系统配置
 * @Author lqy
 * @Date 2023/2/3 16:12
 * @Version 1.0
 **/
public class SysConst {
    // 配置文件所在目录
    public static String rootConfResource = "templates/%s/config";
    // 源代码所在目录
    public static String rootCodeResource = "templates/%s/code";//"classpath:template/config";
    // maven项目资源文件目录，src/main/java
    public static final String codeDirPrefix = "src" + File.separatorChar + "main" + File.separatorChar + "java";
    // maven项目资源文件目录，src/main/resources
    public static final String resourcesDirPrefix = "src" + File.separatorChar + "main" + File.separatorChar + "resources";
    // spring initializr生成的配置文件名称
    public static final String defaultConfigFile = "application.properties";
    // 模板语法正则
    public static final String templateRex = "#【(.*?)】";

}
