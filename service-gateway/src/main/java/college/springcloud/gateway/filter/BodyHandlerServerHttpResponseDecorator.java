package college.springcloud.gateway.filter;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author: xuxianbei
 * Date: 2020/6/12
 * Time: 17:36
 * Version:V1.0
 */
public class BodyHandlerServerHttpResponseDecorator extends ServerHttpResponseDecorator {

    private BodyHandlerFunction bodyHandler = initDefaultBodyHandler();

    public BodyHandlerServerHttpResponseDecorator(BodyHandlerFunction bodyHandler, ServerHttpResponse delegate) {
        super(delegate);
        if (bodyHandler != null) {
            this.bodyHandler = bodyHandler;
        }
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        //body 拦截处理器处理响应
        return bodyHandler.apply(getDelegate(), body);
    }

    @Override
    public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
        return writeWith(Flux.from(body).flatMapSequential(p -> p));
    }

    private BodyHandlerFunction initDefaultBodyHandler() {
        return (resp, body) -> resp.writeWith(body);
    }
}
