package college.springcloud.student.utils;

import feign.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author: guangyu
 * @Date: 2019/1/18 11:20
 * @Description:
 */

//@FeignClient(name = "chenfan-cloud-filestore")
public interface ApiRemoteServer {

    /**
     * 下载
     *
     * @param fileId
     * @return
     */
    @RequestMapping(value = "file/download", method = RequestMethod.GET)
    Response viewImg(@RequestParam("fileId") String fileId);

    @ApiOperation("读取图片")
    @GetMapping({"/file/views/{id}"})
    Response viewsImg(@PathVariable String id);

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @PostMapping(value = "feign/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Response uploadFile(@RequestPart(value = "file") MultipartFile file);


}
