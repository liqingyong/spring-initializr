package #【packageName】.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author lqy
 * @description TODO springboot web 相关配置
 * @date 2023/2/1 16:46
 **/
@Configuration
class WebAppConfiguration implements WebMvcConfigurer {

    /**
     * @author lqy
     * @description TODO 跨域访问配置
     * @date 2023/2/1 16:47
     * @param registry:
     * @return : void
     **/
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")//全部ip可以访问
                .allowedOriginPatterns("*")
                .exposedHeaders("Access-Control-Allow-Headers", "X-Token, Accept, Origin, X-Requested-With, Content-Type, Last-Modified")//返回Header
                .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
                .allowCredentials(true).maxAge(3600);
    }

    /**
     * 注册自定义类型转换器
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Optional<HttpMessageConverter<?>> any = converters.stream().filter(f -> f instanceof MappingJackson2HttpMessageConverter).findAny();
        // 获取Json转换器
        MappingJackson2HttpMessageConverter converter = (MappingJackson2HttpMessageConverter)any.get();
        // 转换Long和long类型 - 避免前端出现number 精度丢失
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, new LongToStringSerializer());
        module.addSerializer(long.class, new LongToStringSerializer());
        // 将自定义序列化器注册进Json转换器中
        converter.getObjectMapper().registerModule(module);
    }

    /**
     * 自定义LongToString序列化器
     */
    public static class LongToStringSerializer extends JsonSerializer<Long> {
        @Override
        public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (null != value) {
                gen.writeString(value.toString());
            } else {
                gen.writeNull();
            }
        }
    }
}
