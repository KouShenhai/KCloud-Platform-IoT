package io.laokou.admin.interfaces.qo;

import io.laokou.common.entity.BasePage;
import lombok.Data;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/11 0011 上午 9:39
 */
@Data
public class SysOauthQO extends BasePage {

    private String clientId;
    private String clientSecret;

}
