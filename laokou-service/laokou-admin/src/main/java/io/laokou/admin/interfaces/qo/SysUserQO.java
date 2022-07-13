package io.laokou.admin.interfaces.qo;
import io.laokou.common.entity.BasePage;
import lombok.Data;
@Data
public class SysUserQO extends BasePage {

    private String username;

    private Integer status;

}
