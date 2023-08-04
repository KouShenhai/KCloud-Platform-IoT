package org.laokou.auth.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.laokou.common.i18n.dto.CommonCommand;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserGetQry extends CommonCommand {

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 登录状态
     */
    private String loginType;

}
