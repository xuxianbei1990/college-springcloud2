package college.springcloud.transport.controller;

import college.springcloud.common.utils.HttpURLConnectionUtil;
import college.springcloud.common.utils.OkHttpUtil;
import college.springcloud.transport.model.OkHttpEntity;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import okhttp3.Request;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.springframework.http.HttpMethod;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: xuxianbei
 * Date: 2019/12/30
 * Time: 15:44
 * Version:V1.0
 */
@RestController
@RequestMapping("/okHttp")
public class OkHttpController {

//    作废：
//    @Bean("restTemplate")
//    public RestTemplate init() {
//        return new RestTemplate();
//    }

    /**
     * 链接超时：默认3000
     */
    @Value("${connectTimeOut:3000}")
    private Long connectTimeOut;

    @Value("${readTimeOut:2000}")
    private Long readTimeOut;

    @Bean("restTemplate")
    public RestTemplate init(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.setConnectTimeout(Duration.ofMillis(connectTimeOut))
                .setReadTimeout(Duration.ofMillis(readTimeOut)).build();
    }

    @Bean("httpRestTemplate")
    public RestTemplate initHttp() {
        /**
         * 需要额外引入包
         * compile("org.apache.httpcomponents:httpcore:4.4.10")
         * compile("org.apache.httpcomponents:httpclient:4.5.6")
         */
        return new CustomHttpRestTemplate();
    }


    @Bean("okRestTemplate")
    public RestTemplate initOk() {
        return new CustomOkHttpRestTemplate();
    }


    @Autowired
    RestTemplate restTemplate;

    @Qualifier("okRestTemplate")
    @Autowired
    RestTemplate okRestTemplate;

    @Qualifier("httpRestTemplate")
    @Autowired
    RestTemplate httpRestTemplate;


    @Autowired
    OkHttpUtil okHttpUtil;

    @PostMapping("/receiveJson")
    public Object receiveJsonController(@RequestBody OkHttpEntity okHttpEntity) {
        return "receive:" + okHttpEntity.toString();
    }

    public static void main(String[] args) throws IOException {
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .build();
//        okhttp3.RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                .addFormDataPart("file","C:\\Users\\2250\\Downloads\\customerData\\1676332481053360.png",
//                        okhttp3.RequestBody.create(MediaType.parse("application/octet-stream"),
//                                new File("C:\\Users\\2250\\Downloads\\customerData\\1676332481053360.png")))
//                .build();
//        Request request = new Request.Builder()
//                .url("http://10.228.81.198:8081/chenfan_api/file/upload")
//                .method("POST", body)
//                .build();
//        Response response = client.newCall(request).execute();
//
//        System.out.println(JSONObject.toJSONString(response));

        uploadFile();
    }

    /**
     * 上传文件
     */
    private static void uploadFile() {
        RestTemplate restTemplate = new CustomOkHttpRestTemplate();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("charset", "UTF-8");
        String url = "http://10.228.81.198:8081/chenfan_api/file/upload";
        MultiValueMap<String, FileSystemResource> map = new LinkedMultiValueMap<>();
        map.add("file", new FileSystemResource("C:\\Users\\2250\\Downloads\\111.xlsx"));
        HttpEntity<MultiValueMap<String, FileSystemResource>> entity = new HttpEntity<>(map, headers);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        ResponseEntity responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println(responseEntity.getBody());
    }

    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 对比测试Okhttp HttpURLConnection http
     * 结论： 请求数100时候。
     * Okhttp:237
     * HttpURLConnection:177
     * spring ok Http:271
     * spring simple Http:217
     * spring Http:92
     * 500
     * Okhttp:1020
     * HttpURLConnection:565
     * spring ok Http:946
     * spring simple Http:911
     * spring  Http:313
     *
     * @param count
     * @return
     */
    @GetMapping("/testJson")
    public String testOkhttpController(@RequestParam(defaultValue = "100") Integer count) {
        OkHttpEntity okHttpEntity = new OkHttpEntity();
        okHttpEntity.setId("x");
        okHttpEntity.setKey("sdf");
        Map<String, Object> params = beanToMap(okHttpEntity);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        HttpEntity<Map<String, String>> requestjson = new HttpEntity(params, httpHeaders);
        String ipaddr = "http://localhost:6032";

        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            okHttpUtil.postJsonParams(ipaddr + "/okHttp/receiveJson",
                    JSONObject.toJSONString(okHttpEntity));
        }
        System.out.println("Okhttp:" + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            Map<String, String> headers = new HashMap<>();
            headers.put("content-type", "application/json");
            headers.put("charset", "UTF-8");
            HttpURLConnectionUtil.sentPost(ipaddr + "/okHttp/receiveJson",
                    JSONObject.toJSONString(okHttpEntity), headers);
        }
        System.out.println("HttpURLConnection:" + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            okRestTemplate.postForObject(ipaddr + "/okHttp/receiveJson",
                    requestjson, String.class);
        }
        System.out.println("spring ok Http:" + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            restTemplate.postForObject(ipaddr + "/okHttp/receiveJson",
                    requestjson, String.class);
        }
        System.out.println("spring simple Http:" + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            httpRestTemplate.postForObject(ipaddr + "/okHttp/receiveJson",
                    requestjson, String.class);
        }
        System.out.println("spring  Http:" + (System.currentTimeMillis() - start));
        System.out.println(httpRestTemplate.postForObject(ipaddr + "/okHttp/receiveJson",
                requestjson, String.class));
        return "succes";
    }

}
