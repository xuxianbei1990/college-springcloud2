package ddd.bookingms.cargotracker.domain.model.valueobjects;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 货物旅程
 *
 * @author: xuxianbei
 * Date: 2021/3/22
 * Time: 9:50
 * Version:V1.0
 */
@Data
public class CargoItinerary {

    public static final CargoItinerary EMPTY_ITINERARY = new CargoItinerary();


    public CargoItinerary() {
        // Nothing to initialize.
    }

    private List<Leg> legs = Collections.emptyList();

    public CargoItinerary(List<Leg> legs) {
        this.legs = legs;
    }
}
