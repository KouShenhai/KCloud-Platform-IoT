package io.laokou.admin.infrastructure.common.feign.elasticsearch.form;

import io.laokou.common.entity.BasePage;
import lombok.Data;

import java.util.List;

/**
 * @author Kou Shenhai
 */
@Data
public class SearchVO<T> extends BasePage {

    private List<T> records;

    /**
     * 数据总条数
     */
    private Long total;

}
