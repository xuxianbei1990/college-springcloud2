package college.springcloud.student;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;

/**
 * word 转换 pdf
 *
 * @author: xuxianbei
 * Date: 2021/9/4
 * Time: 14:44
 * Version:V1.0
 */
public class WordPdfUtils {

    public static void wordExchangePdf() {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream("E:\\整理\\doc2pdf.pdf");
//            String filePath = WordPdfUtils.class.getResource("/exp.docx").getPath();

//            WordprocessingMLPackage mlPackage = WordprocessingMLPackage.load(new File("E:\\整理\\eop-source.docx"));
//            Mapper fontMapper = new IdentityPlusMapper();
//            fontMapper.put("宋体", PhysicalFonts.get("SimSun"));
////            fontMapper.put("新宋体", PhysicalFonts.get("NSimSun"));
////            fontMapper.put("华文宋体", PhysicalFonts.get("STSong"));
////            fontMapper.put("宋体扩展",PhysicalFonts.get("simsun-extB"));
////            fontMapper.put("仿宋_GB2312",PhysicalFonts.get("FangSong_GB2312"));
////            fontMapper.put("仿宋", PhysicalFonts.get("FangSong"));
//            fontMapper.put("微软雅黑", PhysicalFonts.get("Microsoft Yahei"));
//            fontMapper.registerBoldForm("Microsoft Yahei", PhysicalFonts.getBoldForm(PhysicalFonts.get("Microsoft Yahei")));
//            fontMapper.registerBoldForm("SimSun", PhysicalFonts.getBoldForm(PhysicalFonts.get("SimSun")));
//            mlPackage.setFontMapper(fontMapper);
//            FOSettings foSettings = Docx4J.createFOSettings();
//            foSettings.setOpcPackage(mlPackage);
//            Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static InputStream license;
    private static InputStream fileInput;
    private static File outputFile;

    private static boolean getLicense() {
        boolean result = false;
        try {
            System.out.println();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            license = new FileInputStream(WordPdfUtils.class.getResource("/license.xml").getPath());// 凭证文件
            fileInput = new FileInputStream(WordPdfUtils.class.getResource("/eop-source.docx").getPath());// 待处理的文件
            outputFile = new File(System.getProperty("user.dir") + "/eop.pdf");// 输出路径

            License aposeLic = new License();
            aposeLic.setLicense(license);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void wordToPdf() {
        // 验证License
        if (!getLicense()) {
            return;
        }
        try {
            long old = System.currentTimeMillis();
            Document doc = new Document(fileInput);
            FileOutputStream fileOS = new FileOutputStream(outputFile);

            doc.save(fileOS, SaveFormat.PDF);

            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒\n\n" + "文件保存在:" + outputFile.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
//        wordExchangePdf();
        wordToPdf();
    }

}
