package college.springcloud.common.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.util.FileCopyUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        httpHeaders.put("cookie", "thw=cn; t=aaf6b9dc65380d6010dce7b1a08d2511; everywhere_tool_welcome=true; enc=%2FQ5XNZV0suVD2P9okSlxbIAtJ4vgEs6ykOsvld7wmr2xQsAgpRJaQpg46O%2BscH1UFiDfc%2FsOp1dnmM5wtOIAbA%3D%3D; UM_distinctid=17651e1221150b-0ff035cdcdd7a-376b4502-1fa400-17651e1221292e; xlly_s=1; _samesite_flag_=true; cookie2=1fa4dcda6a9e8223010e766d4a1813e8; _tb_token_=e3ebf6313337; XSRF-TOKEN=dd53c95d-2bd2-4612-a9c5-35d233c4f67c; unb=2210801081645; sn=%E7%8F%8A%E7%8F%8A%E5%AE%9D%E8%B4%9D0332008%3A%E9%99%88%E7%94%9C%E7%94%9C; uc1=cookie21=UIHiLt3xSalX&cookie14=Uoe1gW4uTi3ZNg%3D%3D; csg=b3a28bff; skt=894279fe948a4443; _cc_=UIHiLt3xSw%3D%3D; cna=soTKF8CxLxMCAXud3wIUVIZS; _euacm_ac_l_uid_=2210801081645; 2210801081645_euacm_ac_c_uid_=131077237; 2210801081645_euacm_ac_rs_uid_=131077237; _portal_version_=new; cc_gray=1; v=0; _euacm_ac_rs_sid_=68440633; adm_version=new; JSESSIONID=25956BDB343572B3E812CAEE3A2F9B2E; tfstk=cRvCBAspvy4BGeczLHiaLKDlOoC5ZDqfVX_HABWKFVeaHp-CiN2VlGrHZzB-ZG1..; l=fBMqKdFrOoFnG-pWBO5Zhurza77OmIdfCsPzaNbMiIeca6TdpptfsNCIouGvXdtjgTfVeexPkdi_7RnB-fzLRAO-AVKWsQuK_Yv9-bpU-L5..; isg=BPn5hZVmvdsRJ17tq5XC_WsbCGXTBu24GpMXFRsv-iAqohw0YVeviIu8IKZUGoXw");
        httpHeaders.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36");
        //spuID
//        String fileUrl = ("https://sycm.taobao.com/adm/v2/downloadBySchema.do?name=test&logicType=item&dimId=7&themeId=1,2,3,4,5,6,7,8,9,10&startDate=2020-09-22&endDate=2020-10-21&isAutoUpdate=Y&deviceType=0,1,2&indexCode=26" +
//                "&dateType=day&extendAttrs={%22itemId%22:%22628251102956%22}&reportType=1");
        String startDate = "2021-02-19";
        String endDate= "2021-02-22";
        String taobaoSpu = "634917360395,634917688066,634917892099,634921568952,634921920454,634939952943,634940340468,634940564233,635284233964,635284309858,635284837187,635302713329,635617270278,635919943794,635920675617,635920943079,635926735098,635944579176,636503871873,637692856973,637692980825,637693064746,637693064747,637693096693,637693200615,637693308439,637693320438,637693328386,637693344378,637693376261,637693388215,637693460108,637693480086,638050921941,638050989916,638387298914,638387338867,638387378794,638387454665,638387522525,638387538537,638387614434,638387838081," +
                "638718163894,638718319698,638718363609,638718467452,638779363368,638841631935" ;
//                "3452516933327520144,3452886317787161224,3453396976538943477,3453733270381047035,3453733270515282771,3453733274676007798,3453734848915754750,3453735377062505354," +
//                "3453735377196713638,3453735381357432848,3453735381491686877,3454892570126220413,3455060709656943368,3455583866696389098,3455583873004626208,3455615277822276542," +
        String fileUrl = String.format("https://sycm.taobao.com/adm/v2/downloadBySchema.do?name=%s&logicType=item&dimId=7&themeId=1,2,3,4,5,6,7,8,9,10" +
                        "&startDate=%s&endDate=%s&isAutoUpdate=N&deviceType=0,1,2&indexCode=26&dateType=day&extendAttrs=",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "加购数", startDate, endDate) + "{%22itemId%22:%22" + taobaoSpu + "%22}&reportType=1";
//        String fileUrl ="https://sycm.taobao.com/adm/v2/downloadBySchema.do?name=%E6%B5%8B%E8%AF%95001&logicType=item&dimId=7&themeId=1,2,3,4,5,6,7,8,9,10&startDate=2021-02-21&endDate=2021-02-21&isAutoUpdate=N&deviceType=0,1,2&indexCode=26&dateType=day&extendAttrs=" +
//                "{%22itemId%22:%22633147107522%22}&reportType=1";
        Connection connection = Jsoup.connect(fileUrl).headers(httpHeaders);
        Connection.Response response = connection.method(Connection.Method.GET).ignoreContentType(true).timeout(3*1000).execute();
        BufferedInputStream bufferedInputStream = response.bodyStream();
        System.out.println(response.contentType());
        saveFile(bufferedInputStream,"E:\\job\\test.xls");
    }

    public static void main(String[] args) throws IOException {
        exportExcel();
    }

    private static void saveFile(BufferedInputStream bufferedInputStream, String s) {
        byte[] bytes = new byte[0];
        try {
            bytes = FileCopyUtils.copyToByteArray(bufferedInputStream);
            FileCopyUtils.copy(bytes, new File(s));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
