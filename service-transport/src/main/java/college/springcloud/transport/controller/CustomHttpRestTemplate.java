package college.springcloud.transport.controller;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * @author: xuxianbei
 * Date: 2019/12/31
 * Time: 16:36
 * Version:V1.0
 */
public class CustomHttpRestTemplate extends RestTemplate {
    private ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

    public void setRequestFactory(org.springframework.http.client.ClientHttpRequestFactory requestFactory) {
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
