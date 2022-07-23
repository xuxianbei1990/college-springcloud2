package ddd.routingms.infrastructure.repositories.jpa;


import ddd.routingms.domain.model.aggregates.Voyage;

import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 11:26
 * Version:V1.0
 */
public interface VoyageRepository {

    List<Voyage> findAll();
}
