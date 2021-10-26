package college.springcloud.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author: weishili
 */
@Configuration
public class FastJsonConverterConfig {

    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String dateFormat;

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
//        FastJsonConfig config = new FastJsonConfig();
//        config.setSerializerFeatures(
//                SerializerFeature.WriteMapNullValue,
//                SerializerFeature.WriteNullListAsEmpty,
//                SerializerFeature.WriteNullStringAsEmpty,
//                //SerializerFeature.WriteNullBooleanAsFalse,
//                SerializerFeature.WriteDateUseDateFormat,
//                SerializerFeature.DisableCircularReferenceDetect
//        );
//        config.setDateFormat(dateFormat);
//        config.setCharset(Charset.forName("utf-8"));
//        SerializeConfig serializeConfig = new SerializeConfig();
//        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
//        serializeConfig.put(Long.class, ToStringSerializer.instance);
//        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
//        config.setSerializeConfig(serializeConfig);
//        fastJsonHttpMessageConverter.setFastJsonConfig(config);
//        List<MediaType> mediaTypes = Arrays.asList(
//                MediaType.APPLICATION_JSON_UTF8,
//                MediaType.TEXT_PLAIN,
//                MediaType.TEXT_HTML,
//                MediaType.TEXT_XML,
//                MediaType.APPLICATION_OCTET_STREAM);
//        fastJsonHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
        return fastJsonHttpMessageConverter;
    }

//    @Primary
//    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.locale(Locale.getDefault());
            builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            builder.simpleDateFormat(dateFormat);
            // 序列化时过滤为null的字段
            builder.serializationInclusion(JsonInclude.Include.NON_NULL);
            builder.modules(new SwordfishJavaTimeModule());
        };
    }

}