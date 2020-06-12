package college.springcloud.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Objects;

/**
 * @author: xuxianbei
 * Date: 2020/6/12
 * Time: 13:42
 * Version:V1.0
 * GlobalFilter???
 */
//同一级别的过滤器，值越小，优先级越高
//@Order(0)
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    public static final String JWT_ID_HEADER = "leagueId";
    public static final String JWT_SUBJECT_HEADER = "leagueSubject";

    /**
     * 不需要验证token的url
     */
//    @Value("${league.gateway.token.notRequiredAuthUrls}")
    private String notRequiredAuthUrls = "/league/facade/order/**";

//    @Value("${league.gateway.token.closed}")
    private boolean closed = false;

//    @Value("${jwt.admin.secret:}")
    private String secret = "secret_league_admin_>S4aUItz";

    /**
     * 无效的token，返回信息
     *
     * @param exchange
     * @return
     */
    private Mono<Void> invalidResponse(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        DataBuffer buffer = response.bufferFactory().wrap("JSONObject.toJSONString(Result.failure(BizException.UNAUTHORIZED))".getBytes(StandardCharsets.UTF_8));
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String url = request.getURI().getPath();
        log.info("url地址:{}", url);
        // url不能为空
        if (StringUtils.isBlank(url)) {
            log.warn("请求地址为空");
            return invalidResponse(exchange);
        }
        // 关闭token验证
        if (closed) {
            return chain.filter(exchange);
        }
        //尝试解析token
        boolean parseToken = parseToken(request, url);
        // 不需要拦截token
        if (!requiredAuth(notRequiredAuthUrls, url)) {
            return chain.filter(exchange);
        }
        if (!parseToken) {
            return invalidResponse(exchange);
        }
        return chain.filter(exchange);
    }


    private boolean requiredAuth(String notRequiredAuthUrls, String requestUrl) {
        if (StringUtils.isBlank(notRequiredAuthUrls)) {
            return true;
        }
        String[] ignoreUrlsArray = notRequiredAuthUrls.split(",");
        if (ignoreUrlsArray.length == 0) {
            return true;
        }
        //spring ant 路径通配匹配
        AntPathMatcher antPathMatcher = new AntPathMatcher("/");
        for (String url : ignoreUrlsArray) {
            if (antPathMatcher.match(url, requestUrl)) {
                return false;
            }
        }
        return true;
    }

    private boolean parseToken(ServerHttpRequest request, String url) {
        String token = request.getHeaders().getFirst("accessToken");
        log.info("token-request--->{}", token);
        // token为空
        if (StringUtils.isBlank(token)) {
            return false;
        }
        // 解析token
        Claims claims = parseJWT(token, secret);
        if (Objects.isNull(claims)) {
            log.warn("无效的token={}", token);
            return false;
        }
        String id = claims.getId();
        String subject = claims.getSubject();
        // 转发请求时，带上从token解析出的用户信息。
        request.mutate().header(JWT_ID_HEADER, id).build();
        request.mutate().header(JWT_SUBJECT_HEADER, subject).build();
        log.info("token验证通过，本次会话用户ID={}", id);
        return true;
    }

    /**
     * @param id         以用户为例就是用户id
     * @param subject    用户信息 json 字符串
     * @param issuer     身份，发行者，比如：只是商户平台
     * @param secret     秘钥
     * @param expireTime 过期时间
     * @return
     */
    protected static String generateJwt(String id, String subject, String issuer, String secret, Long expireTime) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now).setSubject(subject).setIssuer(issuer).signWith(signatureAlgorithm, signingKey);
        long expMillis = nowMillis + expireTime;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);
        return builder.compact();
    }

    protected Claims parseJWT(String jwt, String sercret) {
        try {
            return (Claims) Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(sercret)).parseClaimsJws(jwt).getBody();
        } catch (Exception var4) {
            log.error("token解析异常", var4);
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(generateJwt("1", "dd", "sky", "secret_league_admin_>S4aUItz", 1000 * 30 * 30 * 60L));
        //eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNTkxOTUyNjUxLCJzdWIiOiJkZCIsImlzcyI6InNreSIsImV4cCI6MTU5MTk1NDQ1MX0.xqxRPXF9YCxG6wctDyweT2DFjZ7YT1QvoyFd5iIYWRI
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
