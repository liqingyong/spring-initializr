package #【packageName】.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author lqy
 * @date 2021/7/9 10:39
 * @description TODO JSON数据处理
 */
@Slf4j
public class JsonObject<T> {
    private Object base;
    private static final ObjectMapper mapper;


    static {
        JsonMapper.Builder builder = JsonMapper.builder();
        // 对于空的对象转json的时候不抛出错误
        builder.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 允许属性名称没有引号
        builder.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许单引号
        builder.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 设置输入时忽略在json字符串中存在但在java对象实际没有的属性
        builder.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 设置输出时包含属性的风格
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        //忽略未知字段
        builder.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 忽略字段大小写
        builder.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper = builder.build();
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }


    public JsonObject(Object base) {
        this.base = base;
    }

    /**
     * json转实体
     * @param json
     * @return
     */
    public static JsonObject parseJsonVo(@NonNull Object json) {
        try {
            Object res = getMapper().readValue(json.toString(), Object.class);
            return new JsonObject<>(getMapper().writeValueAsString(res));
        } catch (Exception e) {
            log.error("转换实体类失败：{}",e.getMessage());
            return new JsonObject<>("{}");
        }
    }


    /**
     * json转实体类
     * @param <T>
     * @param json
     * @param type
     * @return
     */
    public static <T> T parseJson(@NonNull Object json, @NonNull Class<T> type) {
        try {
            return getMapper().readValue(json.toString(), type);
        } catch (Exception e) {
            log.error("json转换失败",e);
            return null;
        }
    }

    /**
     * json转object
     * @param json
     * @return
     */
    public static Object parseObject(@NonNull Object json) {
        try {
            return getMapper().readValue(json.toString(), Object.class);
        } catch (Exception e) {
            log.error("json转换失败",e);
            return null;
        }
    }


    /**
     * json转string
     * @param json
     * @return
     */
    public static String toJsonStr(@NonNull Object json) {
        try {
            return getMapper().writeValueAsString(json);
        } catch (Exception e) {
            log.error("json转换失败",e);
            return null;
        }
    }



    /**
     * json 转map
     * @param json
     * @return
     */
    public static Map parseMap(@NonNull Object json) {
        try {
            return getMapper().readValue(json.toString(), Map.class);
        } catch (Exception e) {
            log.error("json转换失败",e);
            return null;
        }
    }


    /**
     * json字符串转成list
     *
     * @param jsonString
     * @param cls
     * @return
     */
    public static <T> List<T> parseList(@NonNull String jsonString, Class<T> cls) {
        try {
            return getMapper().readValue(jsonString, getCollectionType(List.class, cls));
        } catch (Exception e) {
            String className = cls.getSimpleName();
            log.error(" parse json [{}] to class [{}] error：{}", jsonString, className, e);
        }
        return null;
    }
    /**
     * 转换实体对象
     * @param jsonString
     * @return
     */
    public static  <T> T parseEntity(@NonNull String jsonString, @NonNull Class<T> entity) {
        try {
            return getMapper().readValue(jsonString,entity);
        } catch (IOException e) {
            log.error("json转换失败",e);
            return null;
        }
    }

    /**
     * 获取原始数据
     * @return
     */
    public Object getBase() {
        return base;
    }

    /**
     * 获取数据
     * @param key 键
     * @return
     */
    public Object get(@NonNull Object key) {
        return parseMap(base).get(key);
    }

    public synchronized void put(@NonNull String key,Object val) {
        Map<Object, Object> objectMap = parseMap(base);
        objectMap.put(key,val);
        base = toJsonStr(objectMap);
    }
    /**
     * 获取实体对象
     * @param key 键
     * @return
     */
    public  <T> T getEntity(@NonNull Object key, @NonNull Class<T> entity) {
        try {
            return getMapper().readValue(getStr(key),entity);
        } catch (IOException e) {
            log.error("获取对象失败",e);
            return null;
        }
    }

    /**
     * 获取数据如为空则使用默认值
     * @param key
     * @param d_val
     * @return
     */
    public Object getOrDefault(@NonNull Object key,Object d_val) {
        Object o = get(key);
        return o!=null ? o : d_val;
    }

    /**
     * 获取 Sting 值
     * @param key
     * @return
     */
    public String getStr(@NonNull Object key) {
        try {
            Object o = get(key);
            return o!=null ? o.toString():null;
        } catch (Exception e) {
            log.error("获取对象失败",e);
            return null;
        }
    }

    /**
     * 获取 Long 值
     * @param key
     * @return
     */
    public Long getLong(@NonNull Object key) {
        return Long.valueOf(getStr(key));
    }

    /**
     * @author lqy
     * @description TODO  获取int值
     * @date 15:29 2023/2/1
     * @param key: 键
     * @return: java.lang.Integer
     **/
    public Integer getInt(@NonNull Object key) {
        return Integer.valueOf(getStr(key));
    }

    /**
     * 获取 Boolean 值
     * @param key
     * @return
     */
    public Boolean getBool(@NonNull Object key) {
        return Boolean.valueOf(getStr(key));
    }

    /**
     * 获取实体
     * @param key
     * @return
     */
    public JsonObject getJsonObject(@NonNull Object key) {
        try {
            return new JsonObject( getMapper().writeValueAsString(get(key)));
        } catch (Exception e) {
            log.error("获取对象失败",e);
            return null;
        }
    }


    /**
     * 获取Map对象
     * @param key
     * @param type
     * @return
     */
    public Map<String, T> getMap(@NonNull Object key, Class<T>... type) {
        try {
            return getMapper().readValue(toJsonStr(get(key)), getCollectionType(Map.class, String.class, type != null && type.length > 0 ? type[0] : Object.class));
        } catch (Exception e) {
            log.error("获取对象失败",e);
            return null;
        }
    }

    /**
     * 获取数组
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    public <T> List<T> getList(@NonNull Object key, Class<T> type) {
        try {
            return getMapper().readValue(toJsonStr(get(key)), getCollectionType(List.class, type));
        } catch (Exception e) {
            log.error("获取对象失败",e);
            return null;
        }
    }


    /**
     * 获取泛型的Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  实体bean
     * @return JavaType Java类型
     */
    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {

        return getMapper().getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }


    /*
     * @author lqy 清理数据
     * @description TODO
     * @date 15:30 2023/2/1
     * @return: void
     **/
    public void clear(){
        base = null;
    }

    @Override
    public String toString() {
        return "JsonVo{" + base + '}';
    }
}
