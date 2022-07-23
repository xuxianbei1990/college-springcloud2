package ddd.shareddomain.event;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 10:24
 * Version:V1.0
 */
@Data
public class CargoRoutedEvent {

    private CargoRoutedEventData cargoRoutedEventData;

    public CargoRoutedEvent() {
    }

    public CargoRoutedEvent(CargoRoutedEventData cargoRoutedEventData) {
        this.cargoRoutedEventData = cargoRoutedEventData;
    }
}
