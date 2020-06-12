package college.springcloud.gateway.filter;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

/**
 * @author: xuxianbei
 * Date: 2020/6/12
 * Time: 17:36
 * Version:V1.0
 */
public interface BodyHandlerFunction extends BiFunction<ServerHttpResponse, Publisher<? extends DataBuffer>, Mono<Void>> {
}
