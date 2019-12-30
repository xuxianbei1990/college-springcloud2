package college.springcloud.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

/**
 * @author nick
 * @version 1.0.0
 * @date 2019-12-20
 * @copyright 本内容仅限于深圳市天行云供应链有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/spring")
public class MessagePushListener {

    @Resource
    private Map<String, MsgPushHandler> msgPushHandlerMap;

    @GetMapping("/test1")
    public void websiteNoticeConsumer(){
        getHandler(MsgPushHandler.WEBSITE_MSG_HANDLER);
    }

    @GetMapping("/test2")
    public void systemNoticeInput(){
        getHandler(MsgPushHandler.SYSTEM_MSG_HANDLER);
    }

    @GetMapping("/test3")
    public void optNoticeInput(){
        getHandler(MsgPushHandler.APP_MSG_HANDLER);
    }

    public MsgPushHandler getHandler(String handlerName) {
        return Optional.of(msgPushHandlerMap.get(handlerName)).orElseThrow(() -> new RuntimeException());
    }
}
