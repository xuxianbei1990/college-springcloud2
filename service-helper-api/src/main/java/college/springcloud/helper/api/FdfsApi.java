package college.springcloud.helper.api;

import college.springcloud.common.utils.Result;
import college.springcloud.helper.fallback.FdfsApiFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 */
@FeignClient(value = "xybbc-core-helper", path = "fdfs", fallback = FdfsApiFallBack.class)
public interface FdfsApi {

    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<String> uploadFile(@RequestPart("file") MultipartFile file);

    @PostMapping(value = "/uploadThumImg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<String> uploadThumImg(@RequestPart("file") MultipartFile file);

    @PostMapping("/uploadImageBytes")
    Result<String> uploadFileBytes(@RequestHeader("suffix") String suffix, @RequestBody byte[] fileBytes);

    @PostMapping("/upFile")
    Result<String> upFile(@RequestParam("suffix") String suffix, @RequestBody byte[] fileBytes);

    @PostMapping("/uploadThumImgBytes")
    Result<String> uploadThumImgBytes(@RequestHeader("suffix") String suffix, @RequestBody byte[] imgByte);

    @PostMapping("/download")
    Result<byte[]> download(@RequestParam("filePath") String filePath);

    @PostMapping("/deleteFile")
    Result deleteFile(@RequestParam("filePath") String filePath);
}
