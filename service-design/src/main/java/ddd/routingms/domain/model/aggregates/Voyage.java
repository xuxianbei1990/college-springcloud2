package ddd.routingms.domain.model.aggregates;

import ddd.handlingms.cargotracker.domain.model.valueobjects.VoyageNumber;
import ddd.routingms.domain.model.valueobjects.Schedule;
import lombok.Data;

/**
 * 航行
 *
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 11:29
 * Version:V1.0
 */
@Data
public class Voyage {

    private Long id;

    private Schedule schedule;

    private VoyageNumber voyageNumber;

}
