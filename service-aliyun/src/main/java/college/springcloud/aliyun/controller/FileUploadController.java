package college.springcloud.aliyun.controller;

import college.springcloud.aliyun.config.AliOssConfig;
import college.springcloud.aliyun.utils.AliOSSUtils;
import college.springcloud.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: xuxianbei
 * Date: 2020/5/20
 * Time: 15:23
 * Version:V1.0
 */
@RestController
@RequestMapping("/aliyun")
@Slf4j
public class FileUploadController {

    @Autowired
    private AliOssConfig aliOssConfig;

    @GetMapping(value = "/test")
    public String test() {
        return "test";
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/uploadFile")
    public Result<String> uploadFile(@RequestParam MultipartFile file) {
        String img_name = File.separator + file.getOriginalFilename();
        String url = "";
        try {
            Map<String, InputStream> map = new HashMap<>();
            map.put(img_name, file.getInputStream());
            Map<String, String> urlResult = AliOSSUtils.uploadFileInputStreamList(aliOssConfig, map, aliOssConfig.getUploadPicUrl(), 1);
            url = urlResult.get(img_name);
        } catch (Exception e) {
            log.error("multipart/form-data 单文件上传文件失败");
        }
        return Result.success(StringUtils.join("http://img.league-test.xyb2b.com", "/", url));

    }
}
