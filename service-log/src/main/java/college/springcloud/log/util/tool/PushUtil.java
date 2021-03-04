package college.springcloud.log.util.tool;

import college.springcloud.log.util.start.ApplicationContextUtil;
import io.netty.handler.codec.http.HttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: xuxianbei
 * Date: 2021/3/3
 * Time: 16:53
 * Version:V1.0
 */
public class PushUtil {
    private static final PushUtil INSTANCE = new PushUtil();

    private PushUtil() {

    }

    public static PushUtil getInstance() {
        return INSTANCE;
    }

    public void send(PushUtil.PushType pushType, String templateName, String msg) {

        pushType.sendMsg(templateName, msg);
    }

    /**
     * 先简单写出来
     */
    public enum PushType implements PushNotify {
        DINGDING {
            @Override
            public boolean sendMsg(String templateName, String msg) {
                Map<String, Object> param = new HashMap<>();
                param.put("msgtype", "markdown");
                Map<String, String> markDownParam = new HashMap<>();
                markDownParam.put("title", "系统异常提醒");
                markDownParam.put("text", msg);
                param.put("markdown", markDownParam);
                Map<String, Object> atParam = new HashMap<>();
                atParam.put("isAtAll", true);
                param.put("at", atParam);

                String result;

                if (ApplicationContextUtil.isProd()) {
//                    result = HttpUtil.post("https://oapi.dingtalk.com/robot/send?access_token=45118823a6e2af4dd6172bac3eea0a4771ebec6f2948cae663c7acee552bd56c",
//                            JsonUtil.obj2String(param));
                } else {
//                    result = HttpUtil.post("https://oapi.dingtalk.com/robot/send?access_token=94b8b1e14d3d81b8d4a982deae03f53042fb192ec34338fa2a907f6ab451cb7e",
//                            JsonUtil.obj2String(param));
                }

//                Map map = JsonUtil.string2Obj(result, Map.class);
//                return map.getOrDefault("errcode", 0).equals(0);、
                return true;
            }
        },
        WEIXIN {
            @Override
            public boolean sendMsg(String templateName, String msg) {
                return false;
            }
        }

    }

    public interface PushNotify {
        boolean sendMsg(String templateName, String msg);
    }
}
