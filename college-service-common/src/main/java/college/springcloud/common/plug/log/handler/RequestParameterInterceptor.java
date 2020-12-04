package college.springcloud.common.plug.log.handler;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author: xuxianbei
 * Date: 2020/11/30
 * Time: 16:51
 * Version:V1.0
 */
public class RequestParameterInterceptor extends HttpServletRequestWrapper {

    protected HttpServletRequest orgRequest;

    private Boolean xssCleanFlag;

    // 所有参数的Map集合
    private Map<String, String[]> parameterMap;

    /**
     *
     * XSS攻击
     * @param request
     * @param xssCleanFlag
     */
    public RequestParameterInterceptor(HttpServletRequest request, Boolean xssCleanFlag) {
        super(request);
        this.xssCleanFlag = xssCleanFlag;
        this.orgRequest = request;
        parameterMap = new HashMap<>();
        parameterMap.putAll(request.getParameterMap());
    }

    /**
     * 获取所有参数名
     *
     * @return 返回所有参数名
     */
    @Override
    public Enumeration<String> getParameterNames() {
//        super.getParameterNames()  不用原生的方法和原有的流只能读一次有关
        Vector<String> vector = new Vector<>(parameterMap.keySet());
        return vector.elements();
    }

    /**
     * 获取指定参数名的值，如果有重复的参数名，则返回第一个的值 接收一般变量 ，如text类型
     *
     * @param name
     *            指定参数名
     * @return 指定参数名的值
     */
    @Override
    public String getParameter(String name) {
        String[] results = parameterMap.get(name);
        return results != null ? xssClean(results[0]) : null;
    }

    private String xssClean(String content) {
        return content;

    }

    /**
     * json 参数获取 requestBody
     *
     * @return
     * @throws IOException
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {

        if (orgRequest.getInputStream().available() <= 0) {
            return orgRequest.getInputStream();
        }

        String result = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(orgRequest.getInputStream()))) {
            String line = "";
            StringBuilder lines = new StringBuilder();
            while ((line = br.readLine()) != null) {
                lines.append(xssClean(line));
            }
            result = lines.toString();
        }

        parameterMap.put(String.valueOf(System.nanoTime()), new String[] {result});

        //把原来的inputStream改为ByteArrayInputStream
        return new RequestServletInputStream(new ByteArrayInputStream(result.getBytes()));
    }

    protected class RequestServletInputStream extends ServletInputStream {
        private InputStream stream;

        public RequestServletInputStream(InputStream stream) {
            this.stream = stream;
        }

        public void setStream(InputStream stream) {
            this.stream = stream;
        }

        @Override
        public int read() throws IOException {
            return stream.read();
        }

        @Override
        public boolean isFinished() {
            return true;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {}
    }
}
