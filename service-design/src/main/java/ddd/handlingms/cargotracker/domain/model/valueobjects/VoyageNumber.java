package ddd.handlingms.cargotracker.domain.model.valueobjects;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 19:53
 * Version:V1.0
 */
@Data
public class VoyageNumber {
    private String voyageNumber;

    public VoyageNumber() {
    }

    public VoyageNumber(String voyageNumber) {
        this.voyageNumber = voyageNumber;
    }
}
