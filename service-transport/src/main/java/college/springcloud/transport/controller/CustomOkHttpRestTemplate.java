package college.springcloud.transport.controller;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * @author: xuxianbei
 * Date: 2019/12/31
 * Time: 16:23
 * Version:V1.0
 */
public class CustomOkHttpRestTemplate extends RestTemplate {

    private ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory();

    public void setRequestFactory(ClientHttpRequestFactory requestFactory) {
        Assert.notNull(requestFactory, "ClientHttpRequestFactory must not be null");
        this.requestFactory = requestFactory;
    }

    /**
     * Return the request factory that this accessor uses for obtaining client request handles.
     */
    public ClientHttpRequestFactory getRequestFactory() {
        return this.requestFactory;
    }
}
