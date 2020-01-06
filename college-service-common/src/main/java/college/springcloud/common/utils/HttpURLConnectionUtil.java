package college.springcloud.common.utils;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.Map;


/**
 * http 发送工具
 * xxb
 */
public class HttpURLConnectionUtil {

    private static Logger log = LoggerFactory.getLogger(HttpURLConnectionUtil.class);

    private static final int TIME_OUT = 5;

    /**
     * 使用HTTP POST 发送文本
     *
     * @param httpUrl  发送的地址
     * @param postBody 发送的内容
     * @return 返回HTTP SERVER的处理结果,如果返回null,发送失败
     */
    public static String sentPost(String httpUrl, String postBody) {
        return sentPost(httpUrl, postBody, "UTF-8", null);
    }

    /**
     * 使用HTTP POST 发送文本
     *
     * @param httpUrl  发送的地址
     * @param postBody 发送的内容
     * @return 返回HTTP SERVER的处理结果,如果返回null,发送失败
     */
    public static String sentPost(String httpUrl, String postBody, String encoding) {
        return sentPost(httpUrl, postBody, encoding, null);
    }

    /**
     * 使用HTTP POST 发送文本
     *
     * @param httpUrl   目的地址
     * @param postBody  post的包体
     * @param headerMap 增加的Http头信息
     * @return
     */
    public static String sentPost(String httpUrl, String postBody, Map<String, String> headerMap) {
        return sentPost(httpUrl, postBody, "UTF-8", headerMap);
    }

    /**
     * 使用HTTP POST 发送文本
     *
     * @param httpUrl   发送的地址
     * @param postBody  发送的内容
     * @param encoding  发送的内容的编码
     * @param headerMap 增加的Http头信息
     * @return 返回HTTP SERVER的处理结果,如果返回null,发送失败
     * .................
     */
    public static String sentPost(String httpUrl, String postBody, String encoding, Map<String, String> headerMap) {
        HttpURLConnection httpCon = null;
        String responseBody = null;
        URL url = null;
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e1) {
            return null;
        }
        try {
            httpCon = (HttpURLConnection) url.openConnection();
        } catch (IOException e1) {
            return null;
        }
        if (httpCon == null) {
            return null;
        }
        httpCon.setDoOutput(true);
        httpCon.setConnectTimeout(TIME_OUT * 1000);
        httpCon.setReadTimeout(TIME_OUT * 1000);
        httpCon.setDoOutput(true);
        httpCon.setUseCaches(false);
        try {
            httpCon.setRequestMethod("POST");
        } catch (ProtocolException e1) {
            return null;
        }
        if (headerMap != null) {
            Iterator<Map.Entry<String, String>> iterator = headerMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                httpCon.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        OutputStream output;
        try {
            output = httpCon.getOutputStream();
        } catch (IOException e1) {
            return null;
        }
        try {
            output.write(postBody.getBytes(encoding));
        } catch (UnsupportedEncodingException e1) {
            return null;
        } catch (IOException e1) {
            return null;
        }
        try {
            output.flush();
            output.close();
        } catch (IOException e1) {
            return null;
        }
        // 开始读取返回的内容
        InputStream in;
        try {
            in = httpCon.getInputStream();
        } catch (IOException e1) {
            return null;
        }
        /**
         * 这个方法可以在读写操作前先得知数据流里有多少个字节可以读取。
         * 需要注意的是，如果这个方法用在从本地文件读取数据时，一般不会遇到问题，
         * 但如果是用于网络操作，就经常会遇到一些麻烦。
         * 比如，Socket通讯时，对方明明发来了1000个字节，但是自己的程序调用available()方法却只得到900，或者100，甚至是0，
         * 感觉有点莫名其妙，怎么也找不到原因。
         * 其实，这是因为网络通讯往往是间断性的，一串字节往往分几批进行发送。
         * 本地程序调用available()方法有时得到0，这可能是对方还没有响应，也可能是对方已经响应了，但是数据还没有送达本地。
         * 对方发送了1000个字节给你，也许分成3批到达，这你就要调用3次available()方法才能将数据总数全部得到。
         *
         * 经常出现size为0的情况，导致下面readCount为0使之死循环(while (readCount != -1) {xxxx})，出现死机问题
         */
        int size = 0;
        try {
            size = in.available();
        } catch (IOException e1) {
            return null;
        }
        if (size == 0) {
            size = 1024;
        }
        byte[] readByte = new byte[size];
        // 读取返回的内容
        int readCount = -1;
        try {
            readCount = in.read(readByte, 0, size);
        } catch (IOException e1) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (readCount != -1) {
            baos.write(readByte, 0, readCount);
            try {
                readCount = in.read(readByte, 0, size);
            } catch (IOException e) {
                return null;
            }
        }
        try {
            responseBody = new String(baos.toByteArray(), encoding);
        } catch (UnsupportedEncodingException e) {
            return null;
        } finally {
            if (httpCon != null) {
                httpCon.disconnect();
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                }
            }
        }
        return responseBody;
    }

    /**
     * 发送https请求
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
     */
    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyTrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);

            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error("连接超时：{}", ce);
        } catch (Exception e) {
            log.error("https请求异常：{}", e);
        }
        return jsonObject;
    }

    // 获取body
    public String getBody(HttpServletRequest request) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            CharArrayWriter data = new CharArrayWriter();
            char[] buf = new char[8192];
            int ret;
            while ((ret = in.read(buf, 0, 8192)) != -1) {
                data.write(buf, 0, ret);
            }
            return data.toString();
        } catch (Exception e) {
            System.err.println("接收BODY内容失败：" + e);
        }
        return null;
    }

    public static void main(String[] args) {
//        String url = "http://localhost:8080/app/wechat/callback/app";
//        result.put(WeChatConstant.OUT_TRADE_NO, saveKeys.get(WeChatConstant.OUT_TRADE_NO));
//        StringBuffer xml = WechatConfig.getXMLStringBuffer(result, "test");
//        HttpUtil.sentPost(url, xml.toString(), "UTF-8");

    }
}
