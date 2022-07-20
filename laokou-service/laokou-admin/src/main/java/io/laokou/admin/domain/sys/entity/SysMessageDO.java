package io.laokou.admin.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.laokou.common.entity.BaseDO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 消息管理
 *
 * @author Kou Shenhai
 */
@Data
@TableName("boot_sys_message")
@ApiModel("消息")
public class SysMessageDO extends BaseDO {

    private String username;

    private String title;

    private String content;

    private Integer sendChannel;

}
