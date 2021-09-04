package college.springcloud.student;

import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.web.client.RestTemplate;

import java.io.*;

/**
 * @author: xuxianbei
 * Date: 2021/9/3
 * Time: 15:47
 * Version:V1.0
 */
public class HtmlPdfUtils {


    private static final String ORIG = "uploads/input.html";
    private static final String OUTPUT_FOLDER = "E:\\整理\\Java";


    public static void main(String[] args) throws IOException {

        File htmlSource = new File(OUTPUT_FOLDER +"\\CHIN-EOP.html");
        File pdfDest = new File(OUTPUT_FOLDER + "\\output.pdf");
        HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest));
    }
}
