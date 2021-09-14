package college.springcloud.student;


import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.kernel.counter.event.IMetaInfo;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.DocumentProperties;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;

import java.io.*;

/**
 * @author: xuxianbei
 * Date: 2021/9/3
 * Time: 15:47
 * Version:V1.0
 */
public class HtmlPdfUtils {

    private static final String OUTPUT_FOLDER = "E:\\整理\\Java";


    public static void main(String[] args) throws Exception {

    }

    public static void htmlToPdf(){
        final ConverterProperties converterProperties = defaultFont();
        File htmlSource = new File(HtmlPdfUtils.class.getResource("/uploads/CHIN-EOP.html").getPath());
        File pdfDest = new File(OUTPUT_FOLDER + "\\output.pdf");

        DocumentProperties documentProperties = new DocumentProperties().setEventCountingMetaInfo(new IMetaInfo() {
        });
        PdfDocument pdfDocument = null;
        try {
            pdfDocument = new PdfDocument(new PdfWriter(new FileOutputStream(pdfDest)), documentProperties);
            pdfDocument.setDefaultPageSize(PageSize.A3);
            HtmlConverter.convertToPdf(new FileInputStream(htmlSource), pdfDocument, converterProperties);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
