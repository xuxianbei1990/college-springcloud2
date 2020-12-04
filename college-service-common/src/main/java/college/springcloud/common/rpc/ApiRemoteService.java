package college.springcloud.common.rpc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author mbji
 * @date 2018/12/25
 */
//@FeignClient(name = "chenfan-cloud-api", configuration = FeignUploadConfiguration.class, contextId = "chenfan-cloud-api",
//url = "http://10.228.81.198:8081/chenfan_api/")
public interface ApiRemoteService {


    /**
     * 下载
     *
     * @param fileId id
     * @return 文件
     */
    @GetMapping(value = "file/download")
    feign.Response download(@RequestParam("fileId") String fileId);


    /**
     * 读取压缩图片
     *
     * @param id id
     * @return 文件
     */
    @GetMapping(value = "file/views/{id}")
    feign.Response views(@PathVariable("id") String id);

    /**
     * 下载文件
     *
     * @param fileId 文件ID
     * @return 文件
     */
    @GetMapping("file/downloadWithoutReturnMsg")
    feign.Response downloadWithoutReturnMsg(
            @RequestParam("fileId") String fileId);

}
