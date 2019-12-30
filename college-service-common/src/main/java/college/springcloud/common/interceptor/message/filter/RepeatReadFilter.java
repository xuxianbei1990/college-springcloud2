package college.springcloud.common.interceptor.message.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author: xuxianbei
 * Date: 2019/12/23
 * Time: 13:53
 * Version:V1.0
 */
public class RepeatReadFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * 校验合法
     *
     * @param request
     * @return
     */
    public static boolean CheckoutMethod(HttpServletRequest request) {
        return Objects.equals(request.getContentType(), "application/json") ||
                Objects.equals(request.getContentType(), "text/xml");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        ServletResponse responseWrapper = null;
        if (request instanceof HttpServletRequest) {
            if (CheckoutMethod((HttpServletRequest) request)) {
                requestWrapper = new RepeatReadRequestWrapper((HttpServletRequest) request);
                responseWrapper = new RepeatWriteResponseWrapper((HttpServletResponse) response);
            }
        }
        if (null == requestWrapper) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, responseWrapper);
        }
    }

    @Override
    public void destroy() {

    }
}
