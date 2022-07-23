package ddd.bookingms.cargotracker.domain.model.valueobjects;

import ddd.bookingms.cargotracker.domain.model.entities.Location;
import lombok.Data;

import java.util.Date;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 9:50
 * Version:V1.0
 */
@Data
public class Leg {

    private Voyage voyage;

    private Location loadLocation;

    private Location unloadLocation;

    private Date loadTime;

    private Date unloadTime;

    public Leg(Voyage voyage, Location loadLocation,
               Location unloadLocation, Date loadTime, Date unloadTime) {
        this.voyage = voyage;
        this.loadLocation = loadLocation;
        this.unloadLocation = unloadLocation;
        this.loadTime = loadTime;
        this.unloadTime = unloadTime;

    }
}
