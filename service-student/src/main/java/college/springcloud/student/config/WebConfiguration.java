package college.springcloud.student.config;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2020/4/17
 * Time: 17:12
 * Version:V1.0
 */
@EnableWebMvc
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    /**
     * 结论这个是和JsonSerilize   @JsonFormat 是互斥的
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        SerializerFeature[] serializerFeatures = new SerializerFeature[]{
//                //    输出key是包含双引号
////                SerializerFeature.QuoteFieldNames,
//                //    是否输出为null的字段,若为null 则显示该字段
////                SerializerFeature.WriteMapNullValue,
//                //    数值字段如果为null，则输出为0
//                SerializerFeature.WriteNullNumberAsZero,
//                //     List字段如果为null,输出为[],而非null
//                SerializerFeature.WriteNullListAsEmpty,
//                //    字符类型字段如果为null,输出为"",而非null
//                SerializerFeature.WriteNullStringAsEmpty,
//                //    Boolean字段如果为null,输出为false,而非null
//                SerializerFeature.WriteNullBooleanAsFalse,
//                //    Date的日期转换器
//                SerializerFeature.WriteDateUseDateFormat,
//                //    循环引用
//                SerializerFeature.DisableCircularReferenceDetect,
//        };

//        fastJsonConfig.setSerializerFeatures(serializerFeatures);
//        fastJsonConfig.setSerializerFeatures();
        fastJsonHttpMessageConverter.setSupportedMediaTypes(Lists.newArrayList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8));
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(0, fastJsonHttpMessageConverter);
    }
}
