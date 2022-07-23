package ddd.bookingms.cargotracker.domain.model.entities;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/3/22
 * Time: 9:33
 * Version:V1.0
 */
@Data
public class Location {

    private String unLocCode;

    public Location(String unLocCode) {
        this.unLocCode = unLocCode;
    }
}
