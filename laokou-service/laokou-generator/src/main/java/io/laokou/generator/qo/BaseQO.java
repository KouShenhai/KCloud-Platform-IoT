package io.laokou.generator.qo;

import io.laokou.common.entity.BasePage;
import lombok.Data;

@Data
public class BaseQO extends BasePage {

    String code;
    String tableName;
    String attrType;
    String columnType;
    String connName;
    String dbType;

}
