package college.springcloud.common.interceptor.message.filter;

import college.springcloud.common.interceptor.message.MessageInterceptor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if (request instanceof HttpServletRequest) {
            if (MessageInterceptor.CheckoutMethod((HttpServletRequest) request)) {
                requestWrapper = new RepeatReadRequestWrapper((HttpServletRequest) request);
            }
        }
        if (null == requestWrapper) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }
    }

    @Override
    public void destroy() {

    }
}
