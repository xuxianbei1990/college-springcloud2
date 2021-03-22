package ddd.bookingms.cargotracker.bookingms.domain.model.valueobjects;

import lombok.Data;

/**
 * 货物旅程
 * @author: xuxianbei
 * Date: 2021/3/22
 * Time: 9:50
 * Version:V1.0
 */
@Data
public class CargoItinerary {

    public static final CargoItinerary EMPTY_ITINERARY = new CargoItinerary();
}
