package com.spring.initializr.utils;

import io.spring.initializr.generator.language.java.JavaLanguage;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.version.Version;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @ClassName DescriptionUtil
 * @Description TODO 根据key获取 ProjectDescription
 * @Author lqy
 * @Date 2023/2/3 16:21
 * @Version 1.0
 **/
public class DescriptionUtil<T> {

    /**
     * @author lqy
     * @description TODO 获取Description值
     * @date 2023/2/3 17:34
     * @param key: 输入的参数
     * @param description:  ProjectDescription
     * @return : T 值
     **/
    public static <T> T getDescriptionVal(String key, ProjectDescription description) {
        try {
            String[] split = key.split("\\.");
            return (T) getChildVal(new ArrayList<>(Arrays.asList(split)),description);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @author lqy
     * @description TODO 获取子实例下的值
     * @date 2023/2/3 19:00
     * @param keys: 全部键
     * @param z: 实体
     * @return : T
     **/
    public static <T> T getChildVal(List<String> keys, T z) throws InvocationTargetException, IllegalAccessException {
        Class<?> aClass = z.getClass();
        Method[] methods = aClass.getDeclaredMethods();
        String key = "get"+keys.get(0).toLowerCase(Locale.ROOT);
        for (Method m : methods ) {
            if (key.equals(m.getName().toLowerCase(Locale.ROOT))) {
                m.setAccessible(true);
                Object invoke = m.invoke(z);
                keys.remove(0);
                if (keys.size() > 0)
                    return (T) getChildVal(keys,invoke);
                return (T) invoke;
            }
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
        MutableProjectDescription description = new MutableProjectDescription();
        description.setDescription("asdadsfsdfdasd");
        Version version = new Version(1, 2, 3, new Version.Qualifier("11", 1, "33"));
        description.setPlatformVersion(version);
        description.setLanguage(new JavaLanguage("v.1"));
        String description1 = getDescriptionVal("Description", description);
        String language = getDescriptionVal("Language.jvmVersion", description);
        Object qualifier = getDescriptionVal("platformVersion.qualifier.id", description);
        System.err.println(description1);
        System.err.println(qualifier);

    }

}
