package org.laokou.mongodb.server.form;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.mongodb.client.dto.SearchDTO;

import java.io.Serializable;
import java.util.List;
/**
 * @author laokou
 */
@Data
@Schema(name = "QueryForm",description = "MongoDB查询实体类")
public class QueryForm implements Serializable {

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 条数
     */
    private Integer pageSize = 10;

    /**
     * 模糊条件查询
     */
    private List<SearchDTO> likeSearchList;

    /**
     * 表名
     */
    private String collectionName;

    /**
     * 是否分页
     */
    private boolean needPage = false;



}
