package college.springcloud.log.plug.log.config;

import college.springcloud.log.plug.log.AbstractLog;
import college.springcloud.log.plug.log.handler.RequestParameterInterceptor;
import college.springcloud.log.plug.log.handler.ResponseParameterInterceptor;
import college.springcloud.log.plug.log.level.TraceLog;
import college.springcloud.common.utils.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * filter拦截器
 *
 * @author: xuxianbei
 * Date: 2020/11/30
 * Time: 14:03
 * Version:V1.0
 */
public class FilterConfig {

    private static final String INTERCEPTOR_PATH = "/*";
    private static final Logger logger = LoggerFactory.getLogger(FilterConfig.class);
    private static final String ERROR_MSG = "{\"code\":30001,\"message\":\"用户登陆失效\"}";
    private static String profile;

    private static void write(HttpServletResponse response, byte[] msg) throws IOException {
        if (response.isCommitted()) {
            return;
        }
        response.resetBuffer();
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(msg);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 判断环境 TODO：代修改
     *
     * @return
     */
    public static boolean isDev() {
        return "dev".equals(profile);
    }

    @Value("${spring.profiles:dev}")
    public void setProfile(String profile) {
        FilterConfig.profile = profile;
    }


    /**
     * 注册自定义过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean createCustomFilter(CustomFilter customFilter) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(customFilter);
        filterRegistrationBean.addUrlPatterns(INTERCEPTOR_PATH);
        filterRegistrationBean.setOrder(Integer.MAX_VALUE);
        return filterRegistrationBean;
    }

    /**
     * 定制过滤器
     */
    protected static class CustomFilter implements Filter {
        private final String CLASSNAME = CustomFilter.class.getSimpleName();
        protected Map<String, String> allUrlLine;
        @Autowired
        private LogUrlConfig logUrlConfig;
        private TraceLog traceLog = new TraceLog();

        @Override
        public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {

            if (!isDev()) {
                return;
            }

            Thread thread = new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(10);
                    //产生一个只读的Map
                    this.allUrlLine = Collections.unmodifiableMap(this.logUrlConfig.getAllUrlFile());
                } catch (IOException | InterruptedException var2) {
                }
            });
            //使用守护线程启动？？？
            thread.setDaemon(true);
            thread.start();
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            // 解析用户Id
            String userId = parseUserId(httpServletRequest, httpServletResponse);

            // 跟踪请求
            traceHttp(httpServletRequest, httpServletResponse, chain, userId);
        }

        private void traceHttp(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                               String userId) throws IOException {
            RequestParameterInterceptor newRequest = new RequestParameterInterceptor(request, false);
            ResponseParameterInterceptor newResponse = new ResponseParameterInterceptor(response);

            //放入TraceId
            AbstractLog.traceRequest(newRequest);

            long startTime = System.currentTimeMillis();

            logger.info("========================\\\\                      ");
            logger.info("                         \\\\                     ");
            logger.info("                          \\\\                    ");
            logger.info("RequestURI:        " + request.getRequestURI());
            try {
                chain.doFilter(newRequest, newResponse);
            } catch (Throwable e) {
                // 增加sql
                if (e instanceof SQLException) {
                    traceLog.log(CLASSNAME, "IP={} 执行方法={} sql 异常", IpUtil.getRealIp(request), request.getRequestURI(),
                            e);

                    // 降级操作

                } else {
                    traceLog.log(CLASSNAME, "IP={} 方法={} 全局异常", IpUtil.getRealIp(request), request.getRequestURI(), e);
                }

                // 统一返回错误处理
                write(newResponse, ERROR_MSG.getBytes());
            } finally {
                //感觉这里有bug。
                write(response, newResponse.toByteArray());

                if (allUrlLine != null && isDev()) {
                    String filePath = allUrlLine.get(request.getRequestURI());
                    if (filePath != null && !filePath.isEmpty()) {
                        logger.info("RequestFile:       " + filePath);
                    }
                }

                logger.info("RequestParams:     " + getParamString(newRequest.getParameterMap()));

                logger.info("Total:             " + (System.currentTimeMillis() - startTime) + "ms");
                logger.info("                          //                      ");
                logger.info("                         //                       ");
                logger.info("========================//                        \n\n\n");

                AbstractLog.clearTrace();
            }
        }

        /**
         * 获取参数的字符串形式
         *
         * @param map
         * @return
         */
        private String getParamString(Map<String, String[]> map) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String[]> e : map.entrySet()) {
                sb.append(e.getKey()).append("=");
                String[] value = e.getValue();
                if (value != null && value.length == 1) {
                    sb.append(value[0]).append("&");
                } else {
                    sb.append(Arrays.toString(value)).append("&");
                }
            }
            if (sb.length() >= 1) {
                if (sb.substring(sb.length() - 1, sb.length()).equals("&")) {
                    sb.deleteCharAt(sb.length() - 1);
                }
            }
            return sb.toString();
        }

        /**
         * 解析userId
         *
         * @param request
         * @param response
         * @return
         */
        private String parseUserId(HttpServletRequest request, HttpServletResponse response) {

            String id = null;
            // TODO:代补充ID

            return id;

        }
    }

}
