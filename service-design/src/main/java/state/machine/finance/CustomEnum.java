package state.machine.finance;

/**
 * 枚举花式写法
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 18:18
 * Version:V1.0
 */
public enum CustomEnum implements StatusEvent {
    INVOICE_STATUS_TWELVE(1, "dddd", new StatusEvent() {
        @Override
        public int getStatus() {
            return 0;
        }

        @Override
        public void executeEvent() {

        }
    }) {
        @Override
        public int getStatus() {
            return 0;
        }

        @Override
        public void executeEvent() {

        }
    },
    DDDD {
        @Override
        public int getStatus() {
            return 0;
        }

        @Override
        public void executeEvent() {

        }
    };

    CustomEnum(Integer code, String desc, StatusEvent events) {

    }


    CustomEnum() {

    }
}
