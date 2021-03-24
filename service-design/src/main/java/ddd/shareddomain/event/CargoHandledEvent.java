package ddd.shareddomain.event;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 20:14
 * Version:V1.0
 */
@Data
public class CargoHandledEvent {
    private CargoHandledEventData cargoHandledEventData;
    public CargoHandledEvent(){}
    public CargoHandledEvent(CargoHandledEventData cargoHandledEventData){
        this.cargoHandledEventData = cargoHandledEventData;
    }
}
