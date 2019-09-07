package college.springcloud.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * User: xuxianbei
 * Date: 2019/9/4
 * Time: 20:35
 * Version:V1.0
 */
@Slf4j
public abstract class AbstractJwtManager {

    public static final long TOKEN_EXPIRE_DAY = 86400000L;
    public static final long TOKEN_EXPIRE_WEEK = 604800000L;
    public static final long TOKEN_EXPIRE_MONTH = 2592000000L;

    public AbstractJwtManager() {
    }

    protected String generateJwt(String id, String subject, String issuer, String secret, Long expireTime) {
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
            return (Claims)Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(sercret)).parseClaimsJws(jwt).getBody();
        } catch (Exception var4) {
//            log.error("token解析失败", var4);
            return null;
        }
    }

    public abstract String createJwt(String var1, String var2);

    public abstract String createJwt(String var1, String var2, long var3);

    public abstract Claims parseJwt(String var1);

    public abstract String parseJwtForId(String var1);
}
