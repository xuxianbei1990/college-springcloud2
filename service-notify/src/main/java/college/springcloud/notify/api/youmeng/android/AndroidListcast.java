package college.springcloud.notify.api.youmeng.android;


import college.springcloud.notify.api.youmeng.AndroidNotification;

import java.util.LinkedList;
import java.util.List;

public class AndroidListcast extends AndroidNotification {
    private List<String> list = new LinkedList();

    public AndroidListcast(String appkey, String appMasterSecret) throws Exception {
        setAppMasterSecret(appMasterSecret);
        setPredefinedKeyValue("appkey", appkey);
        this.setPredefinedKeyValue("type", "listcast");
    }

    public void addDeviceToken(String token)  {
        if (list.size() < 500) {
            list.add(token);
        } else {
            throw new RuntimeException("超过友盟最大限制数");
        }
    }

    @Override
    public String getPostBody() {
        try {
            this.setPredefinedKeyValue("device_tokens", String.join(",", list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.getPostBody();
    }
}