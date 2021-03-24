package ddd.routingms.interfaces.rest;


import ddd.routingms.applicaiton.internal.queryservices.CargoRoutingQueryService;
import ddd.routingms.domain.model.aggregates.Voyage;
import ddd.routingms.domain.model.entities.CarrierMovement;
import ddd.shareddomain.model.TransitEdge;
import ddd.shareddomain.model.TransitPath;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 17:10
 * Version:V1.0
 */
@RestController
@RequestMapping("/cargorouting")
public class CargoRoutingController {

    private CargoRoutingQueryService cargoRoutingQueryService;

    /**
     * 最佳路径
     *
     * @param originUnLocode
     * @param destinationUnLocode
     * @param deadline
     * @return
     */
    @GetMapping(path = "/optimalRoute")
    public TransitPath findOptimalRoute(@RequestParam("origin") String originUnLocode,
                                        @RequestParam("destination") String destinationUnLocode,
                                        @RequestParam("deadline") String deadline) {
        List<Voyage> voyages = cargoRoutingQueryService.findAll();

        TransitPath transitPath = new TransitPath();
        List<TransitEdge> transitEdges = new ArrayList<>();
        for (Voyage voyage : voyages) {

            TransitEdge transitEdge = new TransitEdge();
            transitEdge.setVoyageNumber(voyage.getVoyageNumber().getVoyageNumber());
            CarrierMovement movement =
                    ((List<CarrierMovement>) voyage.getSchedule().getCarrierMovements()).get(0);
            transitEdge.setFromDate(movement.getArrivalDate());
            transitEdge.setToDate(movement.getDepartureDate());
            transitEdge.setFromUnLocode(movement.getArrivalLocation().getUnLocCode());
            transitEdge.setToUnLocode(movement.getDepartureLocation().getUnLocCode());
            transitEdges.add(transitEdge);
        }
        transitPath.setTransitEdges(transitEdges);
        return transitPath;
    }
}
