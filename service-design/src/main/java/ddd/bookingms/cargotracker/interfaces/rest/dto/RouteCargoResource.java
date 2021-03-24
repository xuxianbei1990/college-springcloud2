package ddd.bookingms.cargotracker.interfaces.rest.dto;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/3/22
 * Time: 19:42
 * Version:V1.0
 */
@Data
public class RouteCargoResource {
    private String bookingId;

    public RouteCargoResource(){}

    public RouteCargoResource(String bookingId){
        this.bookingId = bookingId;
    }
}
