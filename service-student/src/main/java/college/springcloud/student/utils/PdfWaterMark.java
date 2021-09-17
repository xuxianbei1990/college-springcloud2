package college.springcloud.student.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.*;

import java.io.*;

/**
 * pdf 添加水印
 *
 * @author: xuxianbei
 * Date: 2021/9/17
 * Time: 15:22
 * Version:V1.0
 */
public class PdfWaterMark {

    public static void main(String[] args) {
        try {
            setWatermark(WordToPdfUtil.getProjectPath() + "/output/waterMark.pdf",
                    PdfWaterMark.class.getResourceAsStream("/sample.pdf"), "超级赛亚人", 1);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void setWatermark(String outPut, InputStream input, String word, int model)
            throws DocumentException, IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(outPut)));
        PdfReader reader = new PdfReader(input);
        PdfStamper stamper = new PdfStamper(reader, bos);
        PdfContentByte content;
        // 创建字体,第一个参数是字体路径,itext有一些默认的字体比如说：
        //BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
        BaseFont base = BaseFont.createFont("/font/msyh.ttf", BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED);
        PdfGState gs = new PdfGState();
        // 获取PDF页数
        int total = reader.getNumberOfPages();
        // 遍历每一页
        for (int i = 0; i < total; i++) {
            float width = reader.getPageSize(i + 1).getWidth(); // 页宽度
            float height = reader.getPageSize(i + 1).getHeight(); // 页高度
            content = stamper.getOverContent(i + 1);// 内容
            content.beginText();//开始写入文本
            gs.setFillOpacity(0.5f);//水印透明度
            content.setGState(gs);
            content.setColorFill(BaseColor.LIGHT_GRAY);
            content.setTextMatrix(70, 200);//设置字体的输出位置

            if (model == 1) { //平行居中的3条水印
                content.setFontAndSize(base, 50); //字体大小
                //showTextAligned 方法的参数分别是（文字对齐方式，位置内容，输出水印X轴位置，Y轴位置，旋转角度）
                content.showTextAligned(Element.ALIGN_CENTER, word, width / 2, 650, 30);
                content.showTextAligned(Element.ALIGN_CENTER, word, width / 2, 400, 30);
                content.showTextAligned(Element.ALIGN_CENTER, word, width / 2, 150, 30);
            } else { // 左右两边个从上到下4条水印
                float rotation = 30;// 水印旋转度数

                content.setFontAndSize(base, 20);
                content.showTextAligned(Element.ALIGN_LEFT, word, 20, height - 50, rotation);
                content.showTextAligned(Element.ALIGN_LEFT, word, 20, height / 4 * 3 - 50, rotation);
                content.showTextAligned(Element.ALIGN_LEFT, word, 20, height / 2 - 50, rotation);
                content.showTextAligned(Element.ALIGN_LEFT, word, 20, height / 4 - 50, rotation);

                content.setFontAndSize(base, 22);
                content.showTextAligned(Element.ALIGN_RIGHT, word, width - 20, height - 50, rotation);
                content.showTextAligned(Element.ALIGN_RIGHT, word, width - 20, height / 4 * 3 - 50, rotation);
                content.showTextAligned(Element.ALIGN_RIGHT, word, width - 20, height / 2 - 50, rotation);
                content.showTextAligned(Element.ALIGN_RIGHT, word, width - 20, height / 4 - 50, rotation);
            }
            content.endText();//结束写入文本
            //要打图片水印的话
            //Image image = Image.getInstance("c:/1.jpg");
            //content.addImage(image);
        }
        stamper.close();
    }
}
