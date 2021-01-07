package college.springcloud.common.config;

import college.springcloud.common.config.IdempotentInterceptorAdapter.IdempotentInterceptorUserAdapter;
import college.springcloud.common.interceptor.TokenInterceptor;
import college.springcloud.common.interceptor.message.MessageInterceptor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * mvc 配置中心
 * <p>
 * Interceptor，ViewResolver，MessageConverter
 * User: xuxianbei
 * Date: 2019/8/29
 * Time: 20:16
 * Version:V1.0
 * https://yq.aliyun.com/articles/617307
 */

@EnableSwagger2
@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Autowired
    MessageInterceptor messageInterceptor;

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Autowired
    private IdempotentInterceptorUserAdapter idempotentInterceptorAdapter;

    /**
     * 添加静态资源--过滤swagger-api (开源的在线API文档)
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(new String[]{"/**"}).addResourceLocations(new String[]{"classpath:/static/"});
        registry.addResourceHandler(new String[]{"swagger-ui.html"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/"});
        registry.addResourceHandler(new String[]{"/webjars/**"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/webjars/"});
    }

    //下面代码的意思是：把对象转换成json格式
    //问题是为什么？
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = jackson2HttpMessageConverter.getObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);
        SimpleModule simpleModule = new SimpleModule();
        objectMapper.registerModule(simpleModule);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(0, jackson2HttpMessageConverter);
    }

    @Bean
    public Docket createRestApi() {
        return (new Docket(DocumentationType.SWAGGER_2)).
                apiInfo(this.apiInfo()).select().
                apis(RequestHandlerSelectors.basePackage("college.springcloud")).paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return (new ApiInfoBuilder()).title("xxb  接口文档").description("API").
                termsOfServiceUrl("http://www.xyb2b.com/").contact("深圳行云全球汇").version("1.0").build();
    }

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**");
        registry.addInterceptor(idempotentInterceptorAdapter).addPathPatterns("/**");
    }

}
