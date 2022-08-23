package io.laokou.admin.interfaces.qo;

import io.laokou.common.entity.BasePage;
import lombok.Data;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/19 0019 下午 3:46
 */
@Data
public class SysResourceQO extends BasePage {

    private String title;
    private String code;

}
