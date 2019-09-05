package college.springcloud.helper.controller;

import college.springcloud.common.utils.Result;
import college.springcloud.helper.api.FdfsApi;
import college.springcloud.helper.enums.CoreHelperEnum;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.exception.FdfsServerException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.google.common.base.Strings;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Title 上传文件服务
 * @Package com.xingyun.bbc.core.helper.controller
 * @Description: 上传文件服务
 * @Author Tito
 * @Date 2019/8/19 17:26
 * @Company © 版权所有 深圳市天行云供应链有限公司
 * @Version v1.0
 */
@RestController
@RequestMapping("/fdfs")
public class FdfsController implements FdfsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(FdfsController.class);

    @Value("${fdfs.xy_group}")
    private String group;

    @Resource
    private FastFileStorageClient storageClient;

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @Override
    @PostMapping(value = "/uploadFile")
    public Result<String> uploadFile(@RequestParam MultipartFile file) {
        String img_name = file.getOriginalFilename();
        String suffix = img_name.substring((img_name.lastIndexOf(".") + 1));
        StorePath storePath;
        try {
            storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), suffix, null);
        } catch (Exception e) {
            LOGGER.error("上传文件失败", e);
            return Result.failure(CoreHelperEnum.UPLOAD_FILE_FAILED);
        }
        if (Objects.isNull(storePath) || StringUtils.isBlank(storePath.getPath())) {
            return Result.failure(CoreHelperEnum.UPLOAD_FILE_FAILED);
        }
        return Result.success(storePath.getPath());
    }

    /**
     * 上传图片并生成略缩图
     *
     * @param file
     * @return
     */
    @Override
    @PostMapping(value = "/uploadThumImg")
    public Result<String> uploadThumImg(@RequestParam MultipartFile file) {
        String img_name = file.getOriginalFilename();
        String suffix = img_name.substring((img_name.lastIndexOf(".") + 1));
        StorePath storePath;
        try {
            //会多创建一张略缩图,例:001.jpg-->001_150*150.jpg
            storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), suffix, null);
        } catch (Exception e) {
            LOGGER.error("上传图片并生成略缩图失败", e);
            return Result.failure(CoreHelperEnum.UPLOAD_FILE_FAILED);
        }
        if (Objects.isNull(storePath) || StringUtils.isBlank(storePath.getPath())) {
            return Result.failure(CoreHelperEnum.UPLOAD_FILE_FAILED);
        }
        return Result.success(storePath.getPath());
    }

    /**
     * 上传文件二进制流
     *
     * @param suffix    文件名后缀
     * @param fileBytes 文件字节流
     * @return
     */
    @Override
    @PostMapping(value = "/uploadImageBytes")
    public Result<String> uploadFileBytes(@RequestHeader("suffix") String suffix, @RequestBody byte[] fileBytes) {
        return updateFileByBytes(suffix, fileBytes);
    }


    /**
     * 上传文件二进制流
     *
     * @return
     */
    @Override
    @PostMapping("/upFile")
    public Result<String> upFile(@RequestParam("suffix") String suffix, @RequestBody byte[] fileBytes) {
        return updateFileByBytes(suffix, fileBytes);
    }

    /**
     * 上传图片二进制流并生成略缩图
     *
     * @param suffix
     * @param imgByte
     * @return
     */
    @Override
    @PostMapping(value = "/uploadThumImgBytes")
    public Result<String> uploadThumImgBytes(@RequestHeader("suffix") String suffix, @RequestBody byte[] imgByte) {
        StorePath storePath;
        try {
            //会多创建一张略缩图,例:001.jpeg-->001_150x150.jpeg
            storePath = storageClient.uploadImageAndCrtThumbImage(new ByteArrayInputStream(imgByte), imgByte.length, suffix, null);
        } catch (Exception e) {
            LOGGER.error("上传图片二进制流并生成略缩图失败", e);
            return Result.failure(CoreHelperEnum.UPLOAD_FILE_FAILED);
        }
        if (Objects.isNull(storePath) || StringUtils.isBlank(storePath.getPath())) {
            return Result.failure(CoreHelperEnum.UPLOAD_FILE_FAILED);
        }
        return Result.success(storePath.getPath());
    }

    /**
     * 下载文件
     *
     * @param filePath 文件路径
     */
    @PostMapping(value = "/download")
    @Override
    public Result<byte[]> download(@RequestParam String filePath) {
        try {
            byte[] bytes = storageClient.downloadFile(group, filePath, new DownloadByteArray());
            if (ArrayUtils.isEmpty(bytes)) {
                return Result.failure(CoreHelperEnum.DOWNLOAD_FILE_FAILED);
            }
            return Result.success(bytes);
        } catch (Exception e) {
            LOGGER.info("文件下载失败！文件路径：{}", filePath, e);
            return Result.failure(CoreHelperEnum.DOWNLOAD_FILE_FAILED);
        }
    }

    /**
     * 下载文件
     *
     * @param filePath 文件路径
     * @param response 文件流
     */
    @PostMapping(value = "/downloadFile")
    public void downloadFile(@RequestParam String filePath, HttpServletResponse response) {
        if (Strings.isNullOrEmpty(filePath)) {
            return;
        }
        try (ServletOutputStream out = response.getOutputStream()) {
            byte[] byteArr = storageClient.downloadFile(group, filePath, new DownloadByteArray());
            out.write(byteArr);
            out.flush();
        } catch (IOException e) {
            LOGGER.info("文件下载失败！文件路径：{}", filePath, e);
        }
    }

    /**
     * 打包加载多个文件
     *
     * @param filePaths 文件路径数组
     * @param response
     */
    @PostMapping(value = "/downloadImgsZip")
    public void downloadImgsZip(@RequestParam String[] filePaths, HttpServletResponse response) {
        if (filePaths == null || filePaths.length == 0) {
            return;
        }
        try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()))) {
            for (String filePath : filePaths) {
                String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
                byte[] byteArr = storageClient.downloadFile(group, filePath, new DownloadByteArray());
                if (byteArr == null || byteArr.length == 0) {
                    continue;
                }
                // 创建ZIP实体，并添加进压缩包
                ZipEntry zipEntry = new ZipEntry(fileName);
                zos.putNextEntry(zipEntry);
                // 读取待压缩的文件并写进压缩包里
                int bufferLen = 1024 * 10; // 缓存大小
                byte[] bufs = new byte[bufferLen];
                try (BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(byteArr))) {
                    int read = 0;
                    while ((read = bis.read(bufs, 0, bufferLen)) != -1) {
                        zos.write(bufs, 0, read);
                    }
                } catch (IOException ie) {
                    LOGGER.info("文件打包下载-读取失败！文件路径：{}", filePath, ie);
                    LOGGER.error("文件打包下载-读取失败！文件路径：{}", filePath, ie);
                }
            }
        } catch (IOException oe) {
            LOGGER.info("文件打包下载失败！文件路径集合：{}", Arrays.toString(filePaths), oe);
            LOGGER.error("文件打包下载失败！文件路径集合：{}", Arrays.toString(filePaths), oe);
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return
     */
    @PostMapping(value = "/deleteFile")
    @Override
    public Result deleteFile(@RequestParam String filePath) {
        try {
            storageClient.deleteFile(group, filePath);
        } catch (FdfsServerException e) {
            LOGGER.info("删除文件失败。文件路径：{}", filePath, e);
            LOGGER.error("删除文件失败。文件路径：{}", filePath, e);
            return Result.failure(CoreHelperEnum.DELETE_FILE_FAILED);
        }
        return Result.success();
    }

    /**
     * @param suffix
     * @param fileBytes
     * @return
     */
    private Result<String> updateFileByBytes(String suffix, byte[] fileBytes) {
        StorePath storePath;
        try {
            storePath = storageClient.uploadFile(new ByteArrayInputStream(fileBytes), fileBytes.length, suffix, null);
        } catch (Exception e) {
            LOGGER.error("上传文件二进制流失败", e);
            return Result.failure(CoreHelperEnum.UPLOAD_FILE_FAILED);
        }
        if (Objects.isNull(storePath) || StringUtils.isBlank(storePath.getPath())) {
            return Result.failure(CoreHelperEnum.UPLOAD_FILE_FAILED);
        }
        return Result.success(storePath.getPath());
    }

}
