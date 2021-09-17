package college.springcloud.student;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author: xuxianbei
 * Date: 2021/9/15
 * Time: 15:13
 * Version:V1.0
 */
@Getter
@Slf4j
public enum UploadDataEnum {
    FIRST_GIVE_CONTRACT(1, "初礼合同", "firstGiveContract.docx"),
    ONE_FOR_DISTRIBUTION_CONTRACT(2, "一件代发合同", "oneForDistributionContract.docx"),
    LIVE_COMMON_CONTRACT(3, "直播款-普通合同", "liveCommonContract.docx"),
    LIVE_PREPARE_CONTRACT(4, "直播款-预付合同", "livePrepareContract.docx"),
    MATERIAL_CONTRACT(5, "辅料合同", "materialContract.docx"),
    XUE_LI_LIVE_OUT_CONTRACT(6, "除雪梨生活馆外的成衣合同", "xueLiLiveOutContract.docx"),
    XUE_LI_LIVE_CONTRACT(7, "雪梨生活馆", "xueLiLiveContract.docx");


    private int code;
    private String msg;
    private String fileName;

    UploadDataEnum(int code, String msg, String fileName) {
        this.code = code;
        this.msg = msg;
        this.fileName = fileName;
    }

    public String getFilePath() {
        //解决linux获取文件失败
        InputStream inputStream = UploadDataEnum.class.getResourceAsStream("/templates/" + getFileName());
        File filedir = new File(WordToPdfUtil.getProjectPath() + "\\templates");
        if (!filedir.exists()) {
            filedir.mkdirs();
        }

        File file = new File(WordToPdfUtil.getProjectPath() + "\\templates\\" + getFileName());
        if (!file.exists()) {
            try {
                FileUtils.copyInputStreamToFile(inputStream, file);
            } catch (IOException e) {
                log.error("文件生成异常", e);
            }
        }
        if (file.exists()) {
            return file.getPath();
        } else {
            log.error(filedir.getPath());
            return "";
        }
    }
}
