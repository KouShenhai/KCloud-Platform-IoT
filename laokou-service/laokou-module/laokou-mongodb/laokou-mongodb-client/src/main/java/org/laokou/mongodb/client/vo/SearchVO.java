package org.laokou.mongodb.client.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author laokou
 */
@Data
public class SearchVO<T> implements Serializable {

    private List<T> records;

    /**
     * 数据总条数
     */
    private Long total;

    private Integer pageNum;

    private Integer pageSize;

}
