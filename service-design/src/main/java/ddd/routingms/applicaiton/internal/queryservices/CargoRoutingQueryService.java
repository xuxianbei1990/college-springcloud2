package ddd.routingms.applicaiton.internal.queryservices;


import ddd.routingms.domain.model.aggregates.Voyage;
import ddd.routingms.infrastructure.repositories.jpa.VoyageRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 10:37
 * Version:V1.0
 */
public class CargoRoutingQueryService {

    private VoyageRepository voyageRepository; //

    @Transactional
    public List<Voyage> findAll() {
        return voyageRepository.findAll();
    }


}


