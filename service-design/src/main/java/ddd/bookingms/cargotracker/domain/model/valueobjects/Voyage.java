package ddd.bookingms.cargotracker.domain.model.valueobjects;

import lombok.Data;

/**
 * 航行
 *
 * @author: xuxianbei
 * Date: 2021/3/22
 * Time: 10:32
 * Version:V1.0
 */
@Data
public class Voyage {

    private String voyageNumber;

    public Voyage() {

    }

    public Voyage(String voyageNumber) {
        this.voyageNumber = voyageNumber;
    }
}
