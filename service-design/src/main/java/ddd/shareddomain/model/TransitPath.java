package ddd.shareddomain.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 运输路径
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 9:48
 * Version:V1.0
 */
@Data
public class TransitPath {

    private List<TransitEdge> transitEdges;

    public TransitPath() {
        this.transitEdges = new ArrayList<>();
    }

    public TransitPath(List<TransitEdge> transitEdges) {
        this.transitEdges = transitEdges;
    }
}
