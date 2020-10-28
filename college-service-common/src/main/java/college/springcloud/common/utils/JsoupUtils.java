package college.springcloud.common.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.util.FileCopyUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: xuxianbei
 * Date: 2020/10/27
 * Time: 15:21
 * Version:V1.0
 */
public class JsoupUtils {

    public static void exportExcel() throws IOException {
        Map<String, String> httpHeaders = new HashMap<>();
        httpHeaders.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpHeaders.put("accept-encoding", "gzip, deflate, br");
        httpHeaders.put("accept-language", "zh-CN,zh;q=0.9,ja;q=0.8,en-US;q=0.7,en;q=0.6,zh-TW;q=0.5");
        httpHeaders.put("cache-control", "no-cache");
        httpHeaders.put("pragma", "no-cache");
        httpHeaders.put("sec-fetch-dest", "document");
        httpHeaders.put("sec-fetch-mode", "navigate");
        httpHeaders.put("sec-fetch-site", "none");
        httpHeaders.put("upgrade-insecure-requests", "1");
        httpHeaders.put("cookie", "t=8b6b38e1c1279950806a1865576d49ff; cookie2=19cb20b667d0632b141c955c38cb94a8; _tb_token_=e406e3631733b; _samesite_flag_=true; XSRF-TOKEN=41f7b3ec-867b-417c-897d-474afecddfff; xlly_s=1; unb=2206392900636; sn=lacoco%E5%AE%9A%E5%88%B6%3Abi%E5%88%86%E6%9E%90%E9%83%A8; uc1=cookie21=UtASsssmfufd&cookie14=Uoe0bk6pq75L8w%3D%3D; csg=bd3f5207; skt=b86c3ceaaf58cacc; _cc_=WqG3DMC9EA%3D%3D; _euacm_ac_l_uid_=2206392900636; 2206392900636_euacm_ac_c_uid_=3298891603; 2206392900636_euacm_ac_rs_uid_=3298891603; cna=LCXKF2S9ThQCAXPuK+JRGiKZ; v=0; JSESSIONID=2FFFB2131DDD32ECE5FAE0DAC2D54E54; _euacm_ac_rs_sid_=402650945; adm_version=new; tfstk=cDU1B-9rTdv_-DS2751E_rklsCnAZDOSGGMg1lBTIrT2pXN1iZTrFfUamKLK2X1..; l=eBSF8P04OoFDO-yMXOfZourza779SIRfguPzaNbMiOCP_Y5D5NRAWZWoXBLkCnGVHsNDR3-wPvU8BRLR-yhSnxv9-vxOlgMo3dC..; isg=BD4-VdSV7X578TlcOygxdX3-j1SAfwL5A1xNvuhHrgF7i99lUAv1CT2vB1dHs_oR");
        httpHeaders.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36");
        //spuID
        String fileUrl = ("https://sycm.taobao.com/adm/v2/downloadBySchema.do?name=test&logicType=item&dimId=7&themeId=1,2,3,4,5,6,7,8,9,10&startDate=2020-09-22&endDate=2020-10-21&isAutoUpdate=Y&deviceType=0,1,2&indexCode=26" +
                "&dateType=day&extendAttrs={%22itemId%22:%22628251102956%22}&reportType=1");
        Connection connection = Jsoup.connect(fileUrl).headers(httpHeaders);
        Connection.Response response = connection.method(Connection.Method.GET).ignoreContentType(true).timeout(3*1000).execute();
        BufferedInputStream bufferedInputStream = response.bodyStream();
        System.out.println(response.contentType());
        saveFile(bufferedInputStream,"D:\\test.xls");
    }

    public static void main(String[] args) throws IOException {
        exportExcel();
    }

    private static void saveFile(BufferedInputStream bufferedInputStream, String s) {
        byte[] bytes = new byte[0];
        try {
            bytes = FileCopyUtils.copyToByteArray(bufferedInputStream);
            FileCopyUtils.copy(bytes, new File("E:\\job\\test.xls"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
