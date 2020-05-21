package college.springcloud.aliyun.utils;

import college.springcloud.aliyun.config.AliOssConfig;
import college.springcloud.common.exception.BizException;
import college.springcloud.common.utils.ApplicaitonContextHolder;
import com.alibaba.fastjson.JSON;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author: xuxianbei
 * Date: 2020/5/20
 * Time: 15:27
 * Version:V1.0
 */
public class AliOSSUtils {
    private static final Logger log = LoggerFactory.getLogger(AliOSSUtils.class);
    private static ExecutorService poolService;

    public AliOSSUtils() {
    }

    public static OSS createOssClient(AliOssConfig aliOssConfig) {
        log.info("createOssClient.aliOssConfig={}", JSON.toJSONString(aliOssConfig));
        if (aliOssConfig != null && !StringUtils.isAnyBlank(new CharSequence[]{aliOssConfig.getEndpoint(), aliOssConfig.getAccessKeyId(), aliOssConfig.getAccessKeySecret()})) {
            return (new OSSClientBuilder()).build(aliOssConfig.getEndpoint(), aliOssConfig.getAccessKeyId(), aliOssConfig.getAccessKeySecret());
        } else {
            log.error("阿里云配置不能为null");
            throw new BizException("阿里云配置不能为空");
        }
    }

    public static boolean createBucket(AliOssConfig aliOssConfig) {
        log.info("createBucket.aliOssConfig={}", JSON.toJSONString(aliOssConfig));
        if (aliOssConfig != null && !StringUtils.isBlank(aliOssConfig.getBucketName())) {
            String bucketName = aliOssConfig.getBucketName();
            log.info("创建存储桶bucketName={}", bucketName);
            OSS ossClient = createOssClient(aliOssConfig);
            boolean exists = ossClient.doesBucketExist(bucketName);
            if (!exists) {
                ossClient.createBucket(bucketName);
            }

            return true;
        } else {
            log.error("创建存储桶失败aliOssConfig={}", JSON.toJSONString(aliOssConfig));
            return false;
        }
    }

    public static Map<String, String> uploadFileList(AliOssConfig aliOssConfig, Map<String, String> fileUrlMap) {
        log.info("uploadFileList.aliOssConfig={},fileUrlMap={}", JSON.toJSONString(aliOssConfig), fileUrlMap);
        if (aliOssConfig != null && !StringUtils.isBlank(aliOssConfig.getBucketName()) && fileUrlMap != null) {
            final String bucketName = aliOssConfig.getBucketName();
            final String urlStr = aliOssConfig.getSourceUrl();
            final OSS ossClient = createOssClient(aliOssConfig);
            boolean exists = ossClient.doesBucketExist(aliOssConfig.getBucketName());
            if (!exists) {
                log.error("bucketName={}不存在", bucketName);
                return null;
            } else {
                final CountDownLatch latch = new CountDownLatch(fileUrlMap.size());
                Iterator<Map.Entry<String, String>> entries = fileUrlMap.entrySet().iterator();
                final ConcurrentHashMap aliFileMap = new ConcurrentHashMap();

                while (entries.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry) entries.next();
                    final String fileName = (String) entry.getKey();
                    final String fileUrl = (String) entry.getValue();
                    poolService.submit(new Runnable() {
                        public void run() {
                            FileInputStream inputStream = null;

                            try {
                                inputStream = new FileInputStream(fileUrl);
                                ossClient.putObject(bucketName, fileName, inputStream);
                            } catch (Exception var4) {
                                AliOSSUtils.log.error("文件fileName={}上传失败,error={}", fileName, var4.getMessage());
                                throw new BizException("文件上传阿里云失败，msg=" + var4.getMessage());
                            }

                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException var3) {
                                    AliOSSUtils.log.error("关闭流失败", var3);
                                }
                            }

                            aliFileMap.put(fileName, urlStr + "/" + fileName);
                            latch.countDown();
                        }
                    });
                }

                try {
                    latch.await();
                } catch (Exception var12) {
                    log.error("上传文件countdownlatch.await出错", var12);
                }

                ossClient.shutdown();
                return aliFileMap;
            }
        } else {
            log.error("上传文件失败aliOssConfig={}，fileUrlMap={}", JSON.toJSONString(aliOssConfig), fileUrlMap);
            return null;
        }
    }

    public static void downLoadFileList(AliOssConfig aliOssConfig, Map<String, String> fileNameList) {
        log.info("批量下载文件入参 aliOssConfig={},fileNameList={}", JSON.toJSONString(aliOssConfig), fileNameList);
        if (!CollectionUtils.isEmpty(fileNameList) && aliOssConfig != null) {
            OSS ossClient = createOssClient(aliOssConfig);
            CountDownLatch latch = new CountDownLatch(fileNameList.size());
            new ConcurrentHashMap();
            fileNameList.forEach((k, v) -> {
                poolService.submit(new Runnable() {
                    public void run() {
                        ossClient.getObject(new GetObjectRequest(aliOssConfig.getBucketName(), k), new File(v));
                        latch.countDown();
                    }
                });
            });

            try {
                latch.await();
                ossClient.shutdown();
            } catch (Exception var6) {
                log.error("countdownlatch.await出错", var6);
            }

        } else {
            log.error("文件名不能为空");
        }
    }

    public static String generateIdFileName(String fileType, int randomLen) {
        return joinId(fileType, (String) null, (String) null, randomLen);
    }

    private static String joinId(String orderType, String bizType, String platform, int randomLen) {
        long currentTime = System.currentTimeMillis();
        StringBuffer buffer = new StringBuffer(orderType);
        if (bizType != null && (bizType = bizType.trim()).length() != 0) {
            buffer.append(bizType);
        }

        buffer.append(currentTime - 1000000000000L);
        if (platform != null && (platform = platform.trim()).length() != 0) {
            buffer.append(platform);
        }

        String randomStr = String.valueOf(ThreadLocalRandom.current().nextInt((int) Math.pow(10.0D, (double) randomLen)));

        for (int i = randomStr.length(); i < randomLen; ++i) {
            buffer.append('0');
        }

        buffer.append(randomStr);
        String orderId = buffer.toString();

        return orderId;

    }

    public static Boolean isPic(String name) {
        String reg = "(?i).+?\\.(jpg|bmp|png|jpeg)";
        return name.matches(reg);
    }

    /**
     * size_50： 字体大小
     * t_20: 透明度
     * color_FF0000: 字体颜色
     */
    static String IMAGE_REAL = "image/resize,w_1200/watermark,color_FF0000,size_50,t_20,rotate_330,g_nw,text_" +
//            "/watermark,color_FF0000,size_30,t_50,rotate_330,g_ne,text_" +
            "/watermark,color_FF0000,size_50,t_20,rotate_330,g_center,text_" +
//            "/watermark,color_FF0000,size_30,t_50,rotate_330,g_sw,text_" +
            "/watermark,color_FF0000,size_50,t_20,rotate_330,g_se,text_";
    //阿里云水印内容标志
    static String OSS_WATER_MARK_SUFFIX = "text_";
    //阿里云图片处理标志
    public static final String OSS_FILE_HANDLE_SUFFIX = "x-oss-process";

    public static Map<String, String> uploadFileInputStreamList(AliOssConfig aliOssConfig,
                                                                Map<String, InputStream> fileUrlMap, String uploadPicUrl, Integer waterMarkStyle) {
        log.info("uploadFileList.aliOssConfig={}", JSON.toJSONString(aliOssConfig));

        if (aliOssConfig != null && !StringUtils.isBlank(aliOssConfig.getBucketName())) {
            final String bucketName = aliOssConfig.getBucketName();
            final OSS ossClient = createOssClient(aliOssConfig);
            boolean exists = ossClient.doesBucketExist(aliOssConfig.getBucketName());
            if (!exists) {
                log.error("bucketName={}不存在", bucketName);
                return null;
            } else {
                final CountDownLatch latch = new CountDownLatch(fileUrlMap.size());
                Iterator<Map.Entry<String, InputStream>> entries = fileUrlMap.entrySet().iterator();
                final ConcurrentHashMap aliFileMap = new ConcurrentHashMap();

                while (entries.hasNext()) {
                    Map.Entry<String, InputStream> entry = entries.next();
                    final String fileName = entry.getKey();
                    final InputStream inputStream = entry.getValue();
                    final String newFileName = generateIdFileName("p", 16) + fileName.substring(fileName.lastIndexOf("."));
                    String key = uploadPicUrl + "/" + newFileName;
                    String watermarkKey = uploadPicUrl + "/" + "watermark-" + newFileName;
                    poolService.submit(() -> {
                        try {
                            ossClient.putObject(bucketName, key, inputStream);
                            if (waterMarkStyle > 0) {
                                //水印图片处理
                                waterMakrObject(bucketName, ossClient, key, watermarkKey);
                            }
                        } catch (Exception var2) {
                            AliOSSUtils.log.error("文件fileName={}上传失败,error={}", fileName, var2.getMessage());
                        }
                        aliFileMap.put(fileName, key);
                        if (waterMarkStyle > 0) {
                            aliFileMap.put(fileName, watermarkKey);
                        }
                        latch.countDown();
                    });
                }

                try {
                    latch.await();
                } catch (Exception var13) {
                    log.error("上传文件countdownlatch.await出错", var13);
                }

                ossClient.shutdown();
                return aliFileMap;
            }
        } else {
            log.error("上传文件失败aliOssConfig={}");
            return null;
        }
    }

    private static void waterMakrObject(String bucketName, OSS ossClient, String key, String watermarkKey) throws IOException {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key);
        String watermark = null;

        watermark = ApplicaitonContextHolder.getContext().getEnvironment().getProperty("watermark", "仅供跨境商品清关使用，他用无效");
        watermark = UrlBase64Util.encryptBASE64(watermark);
        String xOssProcess = IMAGE_REAL.replaceAll(OSS_WATER_MARK_SUFFIX, OSS_WATER_MARK_SUFFIX + watermark);
        //图片自动加水印
        if (isPic(key) && StringUtils.isNotBlank(xOssProcess)) {
            Map<String, String> map = new HashMap();
            map.put(OSS_FILE_HANDLE_SUFFIX, xOssProcess);
            generatePresignedUrlRequest.setQueryParameter(map);
            long expireEndTime = System.currentTimeMillis() + 60 * 60 * 1000;
            Date expiration = new Date(expireEndTime);
            generatePresignedUrlRequest.setExpiration(expiration);
            generatePresignedUrlRequest.setMethod(HttpMethod.GET);
            URL url = ossClient.generatePresignedUrl(generatePresignedUrlRequest);
            InputStream inputStream = new ByteArrayInputStream(DownloadnUtil.fetchContentInputStream(url.toString()));
            try {
                ossClient.putObject(bucketName, watermarkKey, inputStream);
            } finally {
                if (Objects.nonNull(inputStream)) {
                    inputStream.close();
                }
            }


        }
    }

    public static Map<String, String> uploadFileInputStreamList(AliOssConfig aliOssConfig, Map<String, InputStream> fileUrlMap, String uploadPicUrl) {
        return uploadFileInputStreamList(aliOssConfig, fileUrlMap, uploadPicUrl, -1);
    }

    public static Map<String, String> uploadByteFileList(AliOssConfig aliOssConfig, Map<String, byte[]> fileUrlMap, String uploadPicUrl) {
        log.info("uploadFileList.aliOssConfig={},fileUrlMap={}", JSON.toJSONString(aliOssConfig));
        if (aliOssConfig != null && !StringUtils.isBlank(aliOssConfig.getBucketName()) && fileUrlMap != null) {
            final String bucketName = aliOssConfig.getBucketName();
            final OSS ossClient = createOssClient(aliOssConfig);
            boolean exists = ossClient.doesBucketExist(aliOssConfig.getBucketName());
            if (!exists) {
                log.error("bucketName={}不存在", bucketName);
                return null;
            } else {
                final CountDownLatch latch = new CountDownLatch(fileUrlMap.size());
                Iterator<Map.Entry<String, byte[]>> entries = fileUrlMap.entrySet().iterator();
                final ConcurrentHashMap aliFileMap = new ConcurrentHashMap();

                while (entries.hasNext()) {
                    Map.Entry<String, byte[]> entry = (Map.Entry) entries.next();
                    final String fileName = (String) entry.getKey();
                    final byte[] fileBuffer = (byte[]) entry.getValue();
                    poolService.submit(new Runnable() {
                        public void run() {
                            try {
                                ossClient.putObject(bucketName, uploadPicUrl + fileName, new ByteArrayInputStream(fileBuffer));
                            } catch (Exception var2) {
                                AliOSSUtils.log.error("文件fileName={}上传失败,error={}", fileName, var2.getMessage());
                            }

                            aliFileMap.put(fileName, fileName);
                            latch.countDown();
                        }
                    });
                }

                try {
                    latch.await();
                } catch (Exception var12) {
                    log.error("上传文件countdownlatch.await出错", var12);
                }

                ossClient.shutdown();
                return aliFileMap;
            }
        } else {
            log.error("上传文件失败aliOssConfig={}，fileUrlMap={}", JSON.toJSONString(aliOssConfig), fileUrlMap);
            return null;
        }
    }

    public static boolean deleteFile(AliOssConfig aliOssConfig, String filePath) {
        String bucketName = aliOssConfig.getBucketName();
        OSS ossClient = createOssClient(aliOssConfig);
        boolean exist = ossClient.doesObjectExist(bucketName, filePath);
        if (!exist) {
            log.error("文件不存在,filePath={}", filePath);
            return false;
        } else {
            log.info("删除文件,filePath={}", filePath);
            ossClient.deleteObject(bucketName, filePath);
            ossClient.shutdown();
            return true;
        }
    }

    public static void downFile(AliOssConfig aliOssConfig, String filePath, HttpServletResponse response) {
        BufferedInputStream input = null;
        OutputStream outputStream = null;
        OSS ossClient = null;

        try {
            ossClient = createOssClient(aliOssConfig);
            OSSObject ossObject = ossClient.getObject(aliOssConfig.getBucketName(), filePath);
            input = new BufferedInputStream(ossObject.getObjectContent());
            byte[] buffBytes = new byte[1024];
            outputStream = response.getOutputStream();
            boolean var8 = false;

            int read;
            while ((read = input.read(buffBytes)) != -1) {
                outputStream.write(buffBytes, 0, read);
            }

            outputStream.flush();
            ossClient.shutdown();
        } catch (Exception var17) {
            var17.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }

                if (input != null) {
                    input.close();
                }
            } catch (IOException var16) {
                var16.printStackTrace();
            }

        }

    }

    static {
        poolService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() + 1,
                Runtime.getRuntime().availableProcessors() + 1, 0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue(100), (r) -> new Thread(r, "Ali-OSS"),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
