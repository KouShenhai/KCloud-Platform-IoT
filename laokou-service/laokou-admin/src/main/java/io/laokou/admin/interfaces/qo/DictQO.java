package io.laokou.admin.interfaces.qo;

import io.laokou.common.entity.BasePage;
import lombok.Data;

import java.io.Serializable;

@Data
public class DictQO extends BasePage implements Serializable {

    private String type;

}
