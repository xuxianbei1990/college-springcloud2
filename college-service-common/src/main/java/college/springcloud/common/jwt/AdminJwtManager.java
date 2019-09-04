package college.springcloud.common.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

/**
 * User: xuxianbei
 * Date: 2019/9/4
 * Time: 20:43
 * Version:V1.0
 */
@Component
public class AdminJwtManager extends AbstractJwtManager {
    //    @Value("${xyjwt.admin.issuer}")
    private String issuer = "issuer_xy_admin_#";
    //    @Value("${xyjwt.admin.secret}")
    private String secret = "secret_xy_admin_#";

    public AdminJwtManager() {
    }

    public String createJwt(String id, String subject) {
        return super.generateJwt(id, subject, this.issuer, this.secret, 86400000L);
    }

    public String createJwt(String id, String subject, long expireTime) {
        return super.generateJwt(id, subject, this.issuer, this.secret, expireTime);
    }

    public Claims parseJwt(String jwt) {
        return super.parseJWT(jwt, this.secret);
    }

    public String parseJwtForId(String jwt) {
        Claims claims = this.parseJwt(jwt);
        return claims == null ? null : claims.getId();
    }
}
