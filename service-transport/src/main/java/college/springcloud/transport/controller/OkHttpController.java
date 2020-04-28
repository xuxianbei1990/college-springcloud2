package college.springcloud.transport.controller;

import college.springcloud.common.utils.HttpURLConnectionUtil;
import college.springcloud.common.utils.OkHttpUtil;
import college.springcloud.transport.model.OkHttpEntity;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

    @Bean("restTemplate")
    public RestTemplate init() {
        return new RestTemplate();
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
        String ipaddr = "http://192.168.2.165:6032";

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
        return "";
    }

}
