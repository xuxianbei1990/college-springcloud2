package college.springcloud.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR;

/**
 * @author: xuxianbei
 * Date: 2020/6/12
 * Time: 17:22
 * Version:V1.0
 * https://www.cnblogs.com/westlin/p/10960251.html
 */
@Order(1)
@Component
@Slf4j
public class LogFilter implements GlobalFilter {

    private long startTime = 1000L;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String trace = exchange.getRequest().getHeaders().getFirst("trace");
        ServerRequest serverRequest = new DefaultServerRequest(exchange);
        log.info("QueryParams:{}",  serverRequest.queryParams());
        return serverRequest.bodyToMono(String.class).flatMap(reqBody -> {
            //重写原始请求
            ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @Override
                public HttpHeaders getHeaders() {
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.putAll(super.getHeaders());
                    return httpHeaders;
                }

                @Override
                public Flux<DataBuffer> getBody() {
                    log.info("[Trace:{}]-gateway request:headers=[{}],body=[{}]", trace, getHeaders(), reqBody);
                    return Flux.just(reqBody).map(bx -> exchange.getResponse().bufferFactory().wrap(bx.getBytes()));
                }
            };
            //重写原始响应
            BodyHandlerServerHttpResponseDecorator responseDecorator = new BodyHandlerServerHttpResponseDecorator(
                    initBodyHandler(exchange, startTime), exchange.getResponse());
            return chain.filter(exchange.mutate().request(decorator).response(responseDecorator).build());
        });
    }
    protected BodyHandlerFunction initBodyHandler(ServerWebExchange exchange, long startTime) {
        return (resp, body) -> {
            String trace = exchange.getRequest().getHeaders().getFirst("trace");
            MediaType originalResponseContentType = exchange.getAttribute(ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(originalResponseContentType);
            DefaultClientResponseAdapter clientResponseAdapter = new DefaultClientResponseAdapter(body, httpHeaders);
            Mono<String> bodyMono = clientResponseAdapter.bodyToMono(String.class);
            return bodyMono.flatMap((respBody) -> {
                //打印返回响应日志
                log.info("[Trace:{}]-gateway response:ct=[{}], status=[{}],headers=[{}],body=[{}]", trace,
                        System.currentTimeMillis() - startTime, resp.getStatusCode(), resp.getHeaders(), respBody);
                return resp.writeWith(Flux.just(respBody).map(bx -> resp.bufferFactory().wrap(bx.getBytes())));
            }).then();
        };
    }
}
