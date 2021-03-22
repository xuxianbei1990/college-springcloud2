package ddd.bookingms.cargotracker.bookingms.domain.model.valueobjects;

/**
 * 预定金额，额外启动一个类？难道因为业务太复杂了？
 *
 * @author: xuxianbei
 * Date: 2021/3/22
 * Time: 9:51
 * Version:V1.0
 */
public class BookingAmount {

    private Integer bookingAmount;

    public BookingAmount(Integer bookingAmount) {
        this.bookingAmount = bookingAmount;
    }
}
