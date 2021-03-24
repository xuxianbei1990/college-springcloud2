package ddd.shareddomain.event;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 10:24
 * Version:V1.0
 */
@Data
public class CargoRoutedEventData {
    private String bookingId;

    public CargoRoutedEventData() {
    }

    public CargoRoutedEventData(String bookingId) {
        this.bookingId = bookingId;
    }
}
