package college.springcloud.notify.api.youmeng;

import college.springcloud.notify.api.youmeng.android.AndroidBroadcast;
import college.springcloud.notify.api.youmeng.android.AndroidListcast;
import college.springcloud.notify.api.youmeng.android.AndroidUnicast;

/**
 * User: xuxianbei
 * Date: 2019/11/29
 * Time: 15:34
 * Version:V1.0
 */
public class YouMengDemo {

    private String appkey = "57d771e367e58e027c00326b";
    private String appMasterSecret = "at8jkkc0gcomnehlinerhfnyme14hsaq";
    private String timestamp = null;
    private PushClient client = new PushClient();

    public void sendAndroidUnicast() throws Exception {
        AndroidUnicast unicast = new AndroidUnicast(appkey,appMasterSecret);
        // TODO Set your device token
        unicast.setDeviceToken("An1Y1j8li-ZbxHwCHntmHk1uXIWIgP7P-y8L-vKX-uop");
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
        listcast.addDeviceToken("An1Y1j8li-ZbxHwCHntmHk1uXIWIgP7P-y8L-vKX-uop");
        listcast.addDeviceToken("An1Y1j8li-ZbxHwCHntmHk1uXIWIgP7P-y8L-vKX-uop");
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


    public static void main(String[] args) throws Exception {
        YouMengDemo youMengDemo = new YouMengDemo();
//        youMeng.sendAndroidUnicast();
        youMengDemo.sendAndroidListcast();
        youMengDemo.sendAndroidBroadcast();
    }
}