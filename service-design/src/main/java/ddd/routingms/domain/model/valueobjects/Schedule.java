package ddd.routingms.domain.model.valueobjects;

import ddd.routingms.domain.model.entities.CarrierMovement;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 17:07
 * Version:V1.0
 */
@Data
public class Schedule {

    public static final Schedule EMPTY = new Schedule();

    private List<CarrierMovement> carrierMovements = Collections.emptyList();

    public List<CarrierMovement> getCarrierMovements() {
        return Collections.unmodifiableList(carrierMovements);
    }
}
