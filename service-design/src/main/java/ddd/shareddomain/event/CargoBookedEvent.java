package ddd.shareddomain.event;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 10:43
 * Version:V1.0
 */
@Data
public class CargoBookedEvent {
    private CargoBookedEventData cargoBookedEventData;
    public CargoBookedEvent(){}
    public CargoBookedEvent(CargoBookedEventData cargoBookedEventData){
        this.cargoBookedEventData  = cargoBookedEventData;
    }
}
