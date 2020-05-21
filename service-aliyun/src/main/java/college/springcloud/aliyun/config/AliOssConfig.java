package college.springcloud.aliyun.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: xuxianbei
 * Date: 2020/5/20
 * Time: 15:33
 * Version:V1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "ali-oss")
public class AliOssConfig {

    /**
     * 域名
     */
    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;

    private String sourceUrl;

    /**
     * 图片上传对应的路径
     */
    private String uploadPicUrl;
}
