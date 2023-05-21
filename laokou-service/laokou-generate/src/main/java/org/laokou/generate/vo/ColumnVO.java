package org.laokou.generate.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ColumnVO implements Serializable {

    private String columnName;
    private String columnComment;

}
