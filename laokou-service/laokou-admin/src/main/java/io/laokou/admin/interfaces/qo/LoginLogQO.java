package io.laokou.admin.interfaces.qo;
import io.laokou.common.entity.BasePage;
import lombok.Data;
@Data
public class LoginLogQO extends BasePage {

    private String loginName;

    private Integer requestStatus;

}
