package ddd.shareddomain.model;

import lombok.Data;

import java.util.Date;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 9:49
 * Version:V1.0
 */
@Data
public class TransitEdge {

    private String voyageNumber;
    private String fromUnLocode;
    private String toUnLocode;
    private Date fromDate;
    private Date toDate;
}
