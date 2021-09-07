package college.springcloud.student;


import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.counter.event.IMetaInfo;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.DocumentProperties;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
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


    public static void main(String[] args) throws Exception {
        final ConverterProperties converterProperties = defaultFont();
        File htmlSource = new File(OUTPUT_FOLDER +"\\CHIN-EOP.html");
        File pdfDest = new File(OUTPUT_FOLDER + "\\output.pdf");
        HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest), converterProperties);
    }




    private static ConverterProperties defaultFont() {
        final ConverterProperties converterProperties = new ConverterProperties();
        final FontProvider fontProvider = new DefaultFontProvider();
        fontProvider.addDirectory("E:\\study\\GitHub\\college-springcloud2\\service-student\\out\\production\\resources\\font");
//        final int ret = fontProvider.addSystemFonts();
        converterProperties.setFontProvider(fontProvider);
        return converterProperties;
    }



}
