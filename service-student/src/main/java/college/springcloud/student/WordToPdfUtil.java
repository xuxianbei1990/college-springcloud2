package college.springcloud.student;

import cn.afterturn.easypoi.word.WordExportUtil;
import college.springcloud.common.utils.Result;
import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.Run;
import com.aspose.words.SaveFormat;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * word转换PDF
 *
 * @author: xuxianbei
 * Date: 2021/9/14
 * Time: 14:08
 * Version:V1.0
 * <p>
 * UploadDataEnum 解决linux文件路径
 */
@Slf4j
@Component
public class WordToPdfUtil {

    public static String getProjectPath() {
        return System.getProperty("user.dir");
    }

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy_MM_dd_HH_mm_ss";

    private AtomicInteger atomicInteger = new AtomicInteger(0);


    private ApiRemoteServer apiRemoteServer;

    public <T> Result execute(String filePath, T t) {
        FileOutputStream fos = null;
        XWPFDocument doc = null;
        FileInputStream fileInput = null;
        FileInputStream filepdf = null;
        File filePdf = null;
        File fileword = null;
        try {
            Map<String, Object> map = beanToMap(t);
            doc = WordExportUtil.exportWord07(filePath, map);
            String tempFile = WordToPdfUtil.getProjectPath() + "\\output";
            File fileDir = new File(tempFile);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            String fileName = getFileName();
            String tempFileWord = tempFile + "/" + fileName + ".docx";
            String tempFilePdf = tempFile + "/" + fileName + ".pdf";
            fos = new FileOutputStream(tempFileWord);
            doc.write(fos);
            fileInput = new FileInputStream(tempFileWord);
            filePdf = new File(tempFilePdf);
            fileword = new File(tempFileWord);
            wordToPdf(fileInput, filePdf);
            filepdf = new FileInputStream(tempFilePdf);
            MultipartFile multipartFile = new StandardMultipartFilePdf(fileName + ".pdf", FileCopyUtils.copyToByteArray(filepdf));
            apiRemoteServer.uploadFile(multipartFile);
            return null;
        } catch (Exception e) {
            log.error("execute异常", e);
            filePdf = null;
            fileword = null;
            throw new RuntimeException("PurchaseExceptionState.WORD_PDF_ERROR");
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (doc != null) {
                    doc.close();
                }
                if (fileInput != null) {
                    fileInput.close();
                }
                if (filepdf != null) {
                    filepdf.close();
                }
                if (filePdf != null && filePdf.exists()) {
                    filePdf.delete();
                }
                if (fileword != null && fileword.exists()) {
                    fileword.delete();
                }
            } catch (Exception e) {
                log.error("文件异常", e);
            }
        }
    }

    private synchronized String getFileName() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS)) +
                atomicInteger.getAndIncrement();
    }

    @SuppressWarnings("serial")
    private static class StandardMultipartFilePdf implements MultipartFile, Serializable {

        private String originalFilename;
        private String name;
        private String contentType;
        private final byte[] content;


        public StandardMultipartFilePdf(String originalFilename, byte[] content) {
            this.name = "file";
            this.originalFilename = originalFilename;
            this.content = content;
            this.contentType = "application/pdf";
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getOriginalFilename() {
            return this.originalFilename;
        }

        @Override
        public String getContentType() {
            return this.contentType;
        }

        @Override
        public boolean isEmpty() {
            return (this.content.length == 0);
        }

        @Override
        public long getSize() {
            return this.content.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return this.content;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(this.content);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            FileCopyUtils.copy(this.content, dest);
        }

        @Override
        public void transferTo(Path dest) throws IOException, IllegalStateException {
            FileCopyUtils.copy(this.content, Files.newOutputStream(dest));
        }
    }

    public boolean getLicense(InputStream license) {
        boolean result = false;
        try {
            License aposeLic = new License();

            aposeLic.setLicense(license);
            result = true;
        } catch (Exception e) {
            log.error("License异常", e);
            throw new RuntimeException("PurchaseExceptionState.WORD_PDF_ERROR");
        }
        return result;
    }

    public <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(String.valueOf(key), beanMap.get(key));
            }
        }
        return map;
    }

    public void wordToPdf(FileInputStream fileInput, File outputFile) {
        InputStream license = WordToPdfUtil.class.getResourceAsStream("/license.xml");
        // 验证License
        if (!getLicense(license)) {
            return;
        }
        FileOutputStream outputStream = null;
        try {
            Document doc = new Document(fileInput);
            outputStream = new FileOutputStream(outputFile);
            doc.save(outputStream, SaveFormat.PDF);
        } catch (Exception e) {
            log.error("wordToPdf异常", e);
            throw new RuntimeException("PurchaseExceptionState.WORD_PDF_ERROR");
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (license != null) {
                    license.close();
                }
            } catch (IOException e) {
                log.error("wordToPdf异常", e);

            }
        }
    }

}
