package io.laokou.admin.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.laokou.common.entity.BaseDO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 消息详情管理
 *
 * @author Kou Shenhai
 */
@Data
@TableName("boot_sys_message_detail")
@ApiModel("消息详情")
public class SysMessageDetailDO extends BaseDO {

    private Long messageId;

    private Long userId;

    private Integer readFlag;

}
