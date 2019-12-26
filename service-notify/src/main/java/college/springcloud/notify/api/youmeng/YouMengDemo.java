package college.springcloud.notify.api.youmeng;

import college.springcloud.notify.api.youmeng.android.AndroidBroadcast;
import college.springcloud.notify.api.youmeng.android.AndroidListcast;
import college.springcloud.notify.api.youmeng.android.AndroidUnicast;
import college.springcloud.notify.api.youmeng.ios.Alert;
import college.springcloud.notify.api.youmeng.ios.IOSBroadcast;
import college.springcloud.notify.api.youmeng.ios.IOSListcast;
import college.springcloud.notify.api.youmeng.ios.IOSUnicast;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * User: xuxianbei
 * Date: 2019/11/29
 * Time: 15:34
 * Version:V1.0
 */
public class YouMengDemo {

//    private String appkey = "57d771e367e58e027c00326b";
//    private String appMasterSecret = "at8jkkc0gcomnehlinerhfnyme14hsaq";
    private String appkey = "57cfdd2767e58e2c260034ba";
    private String appMasterSecret = "oqwvanciersg2ouozmxkn9swh7bbsr76";
    private String timestamp = null;
    private PushClient client = new PushClient();

    public void sendAndroidUnicast() throws Exception {
        AndroidUnicast unicast = new AndroidUnicast(appkey,appMasterSecret);
        // TODO Set your device token
//        unicast.setDeviceToken("An1Y1j8li-ZbxHwCHntmHk1uXIWIgP7P-y8L-vKX-uop");
        unicast.setDeviceToken("AvxBWIKPVnNmzmLohQR-kb9LrkSqeU-yRHPdeypuqSUq");

        unicast.setTicker("Android unicast ticker");
        unicast.setTitle(  "中文的title");
        unicast.setText(   "Android unicast text");
        unicast.goAppAfterOpen();
        unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        // TODO Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        unicast.setProductionMode();
        // Set customized fields
        unicast.setExtraField("test", "helloworld");
        client.send(unicast);
    }

    public void sendAndroidListcast() throws Exception {
        AndroidListcast listcast = new AndroidListcast(appkey,appMasterSecret);
        // TODO Set your device token
//        listcast.addDeviceToken("An1Y1j8li-ZbxHwCHntmHk1uXIWIgP7P-y8L-vKX-uop");
//        listcast.addDeviceToken("An1Y1j8li-ZbxHwCHntmHk1uXIWIgP7P-y8L-vKX-uop");
        listcast.addDeviceToken("AvxBWIKPVnNmzmLohQR-kb9LrkSqeU-yRHPdeypuqSUq");
        listcast.setTicker("Android listcast ticker");
        listcast.setTitle(  "中文的title");
        listcast.setText(   "Android listcast text");
        listcast.goAppAfterOpen();
        listcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        // TODO Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        listcast.setProductionMode();
        // Set customized fields
        listcast.setExtraField("test", "helloworld");
        client.send(listcast);
    }




    public void sendAndroidBroadcast() throws Exception {
        AndroidBroadcast broadcast = new AndroidBroadcast(appkey,appMasterSecret);
        broadcast.setTicker( "Android broadcast ticker");
        broadcast.setTitle(  "中文的title");
        broadcast.setText(   "Android broadcast text");
        broadcast.goAppAfterOpen();
        broadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        // TODO Set 'production_mode' to 'false' if it's a test device.
        // For how to register a test device, please see the developer doc.
        broadcast.setProductionMode();
        // Set customized fields
        broadcast.setExtraField("test", "helloworld");
        client.send(broadcast);
    }


    /**
     * IOS发送单个
     *
     * @return
     * @throws Exception
     */
    public boolean sendIOSUnicast() throws Exception {
        IOSUnicast unicast = new IOSUnicast(appkey, appMasterSecret);
        unicast.setDeviceToken("5e87a794c16b082de27c5f8fa4abc4caa30e618f0da98e02d382aceb4ec8f6bd");
        Alert alert = new Alert();
        alert.setTitle("IOS 单播测试");
        alert.setSubtitle("IOS broadcast Subtitle");
        alert.setBody("IOS broadcast Body");
        unicast.setAlert(JSONObject.toJSONString(alert));
        unicast.setBadge(0);
        unicast.setSound("default");
        unicast.setTestMode();
        // Set customized fields
        unicast.setCustomizedField("test", "helloworld");
        return client.send(unicast);
    }


    @Data
    static class Relation {
        private String frelationType;
        /**
         * 品牌ID
         */
        private String fbrand;
        private String fcategoryName;
        /**
         * 分类ID
         */
        private String funicategoryIds;
        /**
         * 分类层级
         */
        private String fcategoryLevel;
        private String url;
        private String title;
        /**
         * 商品标签
         */
        private String searchFullText;
    }



    /**
     * IOS发送列播
     *
     * @return
     * @throws Exception
     */
    public boolean sendIOSListcast() throws Exception {
        IOSListcast unicast = new IOSListcast(appkey, appMasterSecret);
        unicast.setDeviceToken("5e87a794c16b082de27c5f8fa4abc4caa30e618f0da98e02d382aceb4ec8f6bd");
        Alert alert = new Alert();
        alert.setTitle("IOS broadcast itle");
        alert.setSubtitle("IOS broadcast Subtitle");
        alert.setBody("IOS broadcast Body");
        unicast.setAlert(alert);
        unicast.setBadge(0);
        unicast.setSound("default");
        unicast.setTestMode();
        // Set customized fields
        Relation relation = new Relation();
        relation.setFrelationType("1");
        relation.setFbrand("7538");
        unicast.setCustomizedField("data", JSONObject.toJSONString(relation));
        return client.send(unicast);
    }

    /**
     * 广播
     * @throws Exception
     */
    public void sendIOSBroadcast() throws Exception {
        IOSBroadcast broadcast = new IOSBroadcast(appkey,appMasterSecret);

        broadcast.setAlert("IOS 广播测试");
        broadcast.setBadge( 0);
        broadcast.setSound( "default");
        // TODO set 'production_mode' to 'true' if your app is under production mode
        broadcast.setTestMode();
        // Set customized fields
        broadcast.setCustomizedField("test", "helloworld");
        client.send(broadcast);
    }

    public static void main(String[] args) throws Exception {
        YouMengDemo youMengDemo = new YouMengDemo();
//        youMengDemo.sendAndroidUnicast();
//        youMengDemo.sendAndroidListcast();
//        youMengDemo.sendIOSUnicast();
        youMengDemo.sendIOSListcast();
//        youMengDemo.sendIOSBroadcast();
    }
}
