package college.springcloud.student.utils;

import college.springcloud.student.controller.pdf.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * @author: xuxianbei
 * Date: 2021/9/15
 * Time: 15:13
 * Version:V1.0
 * 文件需要放到templates 目录下
 */
@Getter
@Slf4j
public enum UploadDataEnum {
    FIRST_GIVE_CONTRACT(1, "初礼合同", "firstGiveContract.docx", FirstGiveContractDto.class),
    ONE_FOR_DISTRIBUTION_CONTRACT(2, "一件代发合同", "oneForDistributionContract.docx", OneForDistributionContractDto.class),
    LIVE_COMMON_CONTRACT(3, "直播款-普通合同", "liveCommonContract.docx", LiveCommonContractDto.class),
    LIVE_PREPARE_CONTRACT(4, "直播款-预付合同", "livePrepareContract.docx", LivePrepareContractDto.class),
    MATERIAL_CONTRACT(5, "辅料合同", "materialContract.docx", MaterialContractDto.class),
    XUE_LI_LIVE_OUT_CONTRACT(6, "除雪梨生活馆外的成衣合同", "xueLiLiveOutContract.docx", XueLiLiveOutContractDto.class),
    XUE_LI_LIVE_CONTRACT(7, "雪梨生活馆", "xueLiLiveContract.docx", XueLiLiveContractDto.class);


    private int code;
    private String msg;
    private String fileName;
    private Class aClass;
    private Object value;

    UploadDataEnum(int code, String msg, String fileName, Class aClass) {
        this.code = code;
        this.msg = msg;
        this.fileName = fileName;
        this.aClass = aClass;
    }

    public static UploadDataEnum getSpecial(int code, Object master) {
        Field[] fields = master.getClass().getDeclaredFields();
        for (UploadDataEnum value : values()) {
            if (value.getCode() == code) {
                for (Field field : fields) {
                    if (field.getType().equals(value.getAClass())) {
                        try {
                            boolean boo = field.isAccessible();
                            field.setAccessible(true);
                            value.value = field.get(master);
                            field.setAccessible(boo);
                            if (!StringUtils.isEmpty(value.getFilePath()) && value.value != null) {
                                return value;
                            } else {
                                throw new IllegalArgumentException("文件路径定义异常或者实体定义和约定参数不匹配");
                            }
                        } catch (IllegalAccessException e) {
                            log.error("定义异常", e);
                        }
                    }
                }
            }
        }
        throw new IllegalArgumentException("未定义异常");
    }


    public String getFilePath() {
        //解决linux获取文件失败
        InputStream inputStream = UploadDataEnum.class.getResourceAsStream("/templates/" + getFileName());
        File filedir = new File(WordToPdfUtil.getProjectPath() + "\\templates");
        if (!filedir.exists()) {
            filedir.mkdirs();
        }

        File file = new File(WordToPdfUtil.getProjectPath() + "\\templates\\" + getFileName());
        try {
            FileUtils.copyInputStreamToFile(inputStream, file);
        } catch (IOException e) {
            log.error("文件生成异常", e);
        }

        if (file.exists()) {
            return file.getPath();
        } else {
            log.error(filedir.getPath());
            return "";
        }
    }


}
