package college.springcloud.common.interceptor;

import college.springcloud.common.enums.PermissionEnums;
import college.springcloud.common.jwt.AdminJwtManager;
import college.springcloud.common.model.vo.AccountLoginVo;
import college.springcloud.common.utils.LoginUtil;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * User: xuxianbei
 * Date: 2019/9/3
 * Time: 19:32
 * Version:V1.0
 */
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminJwtManager adminJwtManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //以下代码只是简单实现功能。
        String token = request.getHeader(PermissionEnums.ACCESS_TOKEN.getCode());
        if (StringUtils.isNotBlank(token)) {
            //这里比较费时，可以优化
            Claims claims = adminJwtManager.parseJwt(token);
            if (claims == null) {
                invalidResponse(response, token);
                return false;
            }
            String id = claims.getId();
            String subject = claims.getSubject();
            response.setHeader(PermissionEnums.ACCESS_TOKEN_XYID.getCode(), id);
            response.setHeader(PermissionEnums.ACCESS_TOKEN_XYSUBJECT.getCode(), subject);
            AccountLoginVo accountLoginVo = JSONObject.parseObject(subject, AccountLoginVo.class);
            LoginUtil.getThreadLocal().set(accountLoginVo);
        }
        return true;
    }

    /**
     * 无效的token，返回信息
     *
     * @param token
     * @return
     */
    private boolean invalidResponse(HttpServletResponse response, String token) {
        log.info("无效的token");
        log.error("无效的token");
        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String data = "token 无效";
        //获取OutputStream输出流
        OutputStream outputStream = null;
        byte[] dataByteArr = new byte[0];
        try {
            outputStream = response.getOutputStream();
            //将字符转换成字节数组，指定以UTF-8编码进行转换
            dataByteArr = data.getBytes("UTF-8");
            //使用OutputStream流向客户端输出字节数组
            outputStream.write(dataByteArr);
        } catch (IOException e) {
            e.printStackTrace();
            //先简单这么处理
            log.error(e.getMessage());
        }
        return false;
    }

}
