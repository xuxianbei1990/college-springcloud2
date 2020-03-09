package io.seata.rm;

/**
 * @author: xuxianbei
 * Date: 2020/3/9
 * Time: 16:01
 * Version:V1.0
 */
public class DefaultRMHandler extends AbstractRMHandler {


    public static AbstractRMHandler get() {
        return SingletomHolder.INSTANCE.get();
    }

    enum SingletomHolder {
        INSTANCE(new DefaultRMHandler());

        SingletomHolder(DefaultRMHandler rmHandler) {
            this.single = rmHandler;
        }

        private DefaultRMHandler single;

        public DefaultRMHandler get() {
            return single;
        }
    }

}
