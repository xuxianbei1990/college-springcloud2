package college.springcloud.log.plug.log.handler;

import org.apache.commons.io.output.TeeOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 让原来的流变成可以重复读取的流
 * @author: xuxianbei
 * Date: 2020/11/30
 * Time: 17:14
 * Version:V1.0
 */
public class ResponseParameterInterceptor extends HttpServletResponseWrapper {

    private ByteArrayOutputStream output;
    private ServletOutputStream filterOutput;

    private OutputStream bufferOutputStream;

    public ResponseParameterInterceptor(HttpServletResponse response) throws IOException {
        super(response);
        bufferOutputStream = response.getOutputStream();

        output = new ByteArrayOutputStream();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (filterOutput == null) {
            filterOutput = new ServletOutputStream() {
                //分流，就是对TeeOutputStream操作，就会同时对bufferOutputStream, output 操作
                private TeeOutputStream teeOutputStream = new TeeOutputStream(bufferOutputStream, output);

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {

                }

                @Override
                public void write(int b) throws IOException {
                    teeOutputStream.write(b);
                }
            };
        }
        return filterOutput;
    }

    public byte[] toByteArray() {
        return output.toByteArray();
    }
}
