package college.springcloud.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 获取真实Ip
 */
@Slf4j
public class Iptool {

    private Iptool() {
    }

    private static boolean isNotEmpty(String str) {
        return str != null && str.length() != 0;

    }

    private static boolean isEmpty(String str) {
        return !isNotEmpty(str);
    }

    public static String getRealIp(HttpServletRequest request) {
        String ipAddress = "";
        try {
            if (request == null) {
                return ipAddress;
            }
            // 阿里云代理的先获取
            ipAddress = request.getHeader("X-Forwarded-For");
            if (isNotEmpty(ipAddress)) {
                // 获取第一个IP
                if (ipAddress.contains(",")) {
                    for (String ip_add : ipAddress.split(",")) {
                        ipAddress = ip_add;
                        if (isNotEmpty(ipAddress)) {
                            break;
                        }
                    }
                }

            } else {
                ipAddress = request.getHeader("x-forwarded-for");
                if (isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
                    ipAddress = request.getHeader("Proxy-Client-IP");
                }
                if (isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
                    ipAddress = request.getHeader("WL-Proxy-Client-IP");
                }
                if (isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
                    ipAddress = request.getRemoteAddr();
                }
                if (isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
                    ipAddress = request.getHeader("X-Real-IP");
                }
            }

            if (ipAddress != null && ipAddress.contains("0:0:0:0:0:0:0:1")) {
                // 根据网卡取本机配置的IP

                // linux下也可以获取本地的ip地址
                Enumeration<?> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
                InetAddress ip;
                while (allNetInterfaces.hasMoreElements()) {
                    NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip != null && ip instanceof Inet4Address) {
                            // 获取真实的Ip地址
                            ipAddress = ip.getHostAddress();

                        }
                    }
                }

            }

            // 多个路由时，取第一个非unknown的ip
            final String[] arr = ipAddress.split(",");
            for (final String str : arr) {
                if (!"unknown".equalsIgnoreCase(str)) {
                    ipAddress = str;
                    break;
                }
            }

        } catch (Exception e) {
            log.error("ip{},解析异常", ipAddress, e);
        }
        return ipAddress;

    }

}
