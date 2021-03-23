package ddd.bookingms.cargotracker.bookingms.application.internal.outboundservices.acl;

import ddd.bookingms.cargotracker.bookingms.domain.model.entities.Location;
import ddd.bookingms.cargotracker.bookingms.domain.model.valueobjects.CargoItinerary;
import ddd.bookingms.cargotracker.bookingms.domain.model.valueobjects.Leg;
import ddd.bookingms.cargotracker.bookingms.domain.model.valueobjects.RouteSpecification;
import ddd.bookingms.cargotracker.bookingms.domain.model.valueobjects.Voyage;
import ddd.bookingms.cargotracker.shareddomain.model.TransitEdge;
import ddd.bookingms.cargotracker.shareddomain.model.TransitPath;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 9:45
 * Version:V1.0
 */
public class ExternalCargoRoutingService {


    public CargoItinerary fetchRouteForSpecification(RouteSpecification routeSpecification){
        RestTemplate restTemplate = new RestTemplate();
        Map<String,Object> params = new HashMap<>();
        params.put("origin",routeSpecification.getOrigin().getUnLocCode());
        params.put("destination",routeSpecification.getDestination().getUnLocCode());
        params.put("deadline",routeSpecification.getArrivalDeadline().toString());

        TransitPath transitPath = restTemplate.getForObject("http://localhost:8081/cargorouting/optimalRoute?origin=&destination=&deadline=",
                TransitPath.class);


        List<Leg> legs = new ArrayList<>(transitPath.getTransitEdges().size());
        for (TransitEdge edge : transitPath.getTransitEdges()) {
            legs.add(toLeg(edge));
        }

        return new CargoItinerary(legs);
    }

    private Leg toLeg(TransitEdge edge) {
        return new Leg(
                new Voyage(edge.getVoyageNumber()),
                new Location(edge.getFromUnLocode()),
                new Location(edge.getToUnLocode()),
                edge.getFromDate(),
                edge.getToDate());
    }
}
