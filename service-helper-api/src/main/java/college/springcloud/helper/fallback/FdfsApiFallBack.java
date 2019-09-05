package college.springcloud.helper.fallback;


import college.springcloud.common.utils.Result;
import college.springcloud.helper.api.FdfsApi;
import college.springcloud.helper.enums.CoreHelperEnum;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 */

public class FdfsApiFallBack implements FdfsApi {


    @Override
    public Result<String> uploadFile(MultipartFile file) {
        return Result.failure(CoreHelperEnum.HELPER_CONNECT_FAILED);
    }

    @Override
    public Result<String> uploadThumImg(MultipartFile file) {
        return Result.failure(CoreHelperEnum.HELPER_CONNECT_FAILED);
    }

    @Override
    public Result<String> uploadFileBytes(String suffix, byte[] fileBytes) {
        return Result.failure(CoreHelperEnum.HELPER_CONNECT_FAILED);
    }

    @Override
    public Result<String> upFile(String suffix, @RequestBody byte[] fileBytes) {
        return Result.failure(CoreHelperEnum.HELPER_CONNECT_FAILED);
    }

    @Override
    public Result<String> uploadThumImgBytes(String suffix, byte[] imgByte) {
        return Result.failure(CoreHelperEnum.HELPER_CONNECT_FAILED);
    }

    @Override
    public Result<byte[]> download(String filePath) {
        return Result.failure(CoreHelperEnum.HELPER_CONNECT_FAILED);
    }

    @Override
    public Result deleteFile(String filePath) {
        return Result.failure(CoreHelperEnum.HELPER_CONNECT_FAILED);
    }
}
