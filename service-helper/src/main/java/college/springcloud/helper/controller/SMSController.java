package college.springcloud.helper.controller;

import college.springcloud.common.enums.ResultStatus;
import college.springcloud.common.utils.Result;
import college.springcloud.helper.po.Sms;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName: SMSController
 * @Description: 发送短信
 * @author ZSY
 * @date 2019年09月04日 上午11:50:36
 *
 */
@RestController
@RequestMapping(value = "/sms")
public class SMSController {
    private static Logger logger = LoggerFactory.getLogger(SMSController.class);

    @Autowired
    private Sms sms;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/sendSms")
    public Result<Boolean> sendSms(@RequestParam("mobile") String mobile, @RequestParam("content") String content){
        JSONObject params = new JSONObject();
        params.put(sms.getUsernameKey(), sms.getUsernameValue());
        params.put(sms.getPasswordKey(), sms.getPasswordValue());
        params.put(sms.getMobileKey(), mobile);
        params.put(sms.getContentKey(), content);
        params.put(sms.getReportKey(), "false");
        params.put(sms.getExtendkey(), null);
        String reply = "";
        try{
            reply = restTemplate.postForObject(sms.getUrl(), params, String.class);
        }catch (Exception e){
            logger.error("发送失败，错误信息：服务平台或者短信平台异常" + "，手机：" + mobile + "，短信内容：" + content);
            Result.failure(ResultStatus.NOT_IMPLEMENTED);
        }

        logger.info("发送短信,手机: " + mobile + "返回结果: " + reply + "短信内容: " + content);
        if(Strings.isNullOrEmpty(reply)){
            logger.error("发送失败，错误信息：服务平台或者短信平台异常" + "，手机：" + mobile + "，短信内容：" + content);
            Result.failure(ResultStatus.NOT_IMPLEMENTED);
        }
        int replyStatus = JSON.parseObject(reply).getInteger("code");
        if(replyStatus != 0){
            logger.error("发送失败，错误信息：" + replyStatus + "，手机：" + mobile + "，短信内容：" + content);
            Result.failure(ResultStatus.NOT_IMPLEMENTED);
        }
        return Result.success();
    }

}
