package college.springcloud.config;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.StringCodec;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2021/9/29
 * Time: 11:36
 * Version:V1.0
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private FastJsonHttpMessageConverter fastJsonHttpMessageConverter;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(fastJsonHttpMessageConverter);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        if (CollectionUtils.isNotEmpty(converters)) {
            converters.forEach(converter -> {
                //接口（反）序列化-字符串字段前后去空格
                if (converter instanceof FastJsonHttpMessageConverter) {
                    trimString((FastJsonHttpMessageConverter) converter);
                    codeExchange((FastJsonHttpMessageConverter) converter);

                }
            });
        }
    }

    private void codeExchange(FastJsonHttpMessageConverter converter) {
        IntegerCodec integerCodec = new IntegerCodec(){
            @Override
            public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
                return super.deserialze(parser, clazz, fieldName);
            }

            @Override
            public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
                super.write(serializer, object, fieldName, fieldType, features);
            }
        };
        FastJsonConfig fastJsonConfig = converter.getFastJsonConfig();
        fastJsonConfig.getSerializeConfig().put(Integer.class, integerCodec);
    }

    private void trimString(FastJsonHttpMessageConverter converter) {
        StringCodec trimStringCodec = new StringCodec() {
            @Override
            public String deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
                return StringUtils.trim(super.deserialze(parser, clazz, fieldName));
            }

            @Override
            public void write(JSONSerializer serializer, String value) {
                super.write(serializer, StringUtils.trim(value));
            }
        };
        FastJsonConfig fastJsonConfig = converter.getFastJsonConfig();
        fastJsonConfig.getParserConfig().putDeserializer(String.class, trimStringCodec);
        fastJsonConfig.getParserConfig().putDeserializer(StringBuffer.class, trimStringCodec);
        fastJsonConfig.getParserConfig().putDeserializer(StringBuilder.class, trimStringCodec);
        //com.chenfan.common.config.FastJsonConverterConfig 重置了
        fastJsonConfig.getSerializeConfig().put(String.class, trimStringCodec);
        fastJsonConfig.getSerializeConfig().put(StringBuffer.class, trimStringCodec);
        fastJsonConfig.getSerializeConfig().put(StringBuilder.class, trimStringCodec);
    }
}
