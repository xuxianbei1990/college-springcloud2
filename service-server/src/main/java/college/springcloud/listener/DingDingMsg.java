package college.springcloud.listener;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2020/4/28
 * Time: 11:57
 * Version:V1.0
 */
@Data
public class DingDingMsg {
    private String msgtype;
    private Text text = new Text();
    private At at = new At();

    @Data
    @Accessors(chain = true)
    static class Text {
        private String content;
    }

    @Data
    static class At {
        private List<String> atMobiles = new ArrayList<>();
        private Boolean isAtAll = false;
    }
}
