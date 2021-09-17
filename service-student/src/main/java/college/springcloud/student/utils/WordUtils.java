package college.springcloud.student.utils;

import cn.afterturn.easypoi.word.WordExportUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: xuxianbei
 * Date: 2021/9/4
 * Time: 14:49
 * Version:V1.0
 */
public class WordUtils {

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap();
        map.put("title", "阿斯特拉");
        map.put("orderNum", "XDF1235547");
        List<Map<String, String>> goods = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, String> item = new HashMap<>();
            item.put("name", "商品" + i);
            item.put("price", String.valueOf((int) (Math.random() * 10)));
            goods.add(item);
        }
        map.put("goods", goods);
        try {
            String filePath = WordUtils.class.getResource("/test.docx").getPath();
            XWPFDocument xwpfDocument = WordExportUtil.exportWord07(filePath, map);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            xwpfDocument.write(outputStream);
            FileOutputStream fos = new FileOutputStream("E:\\整理\\exp.docx");

            fos.write(outputStream.toByteArray());
            fos.flush();
            outputStream.close();
            fos.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
