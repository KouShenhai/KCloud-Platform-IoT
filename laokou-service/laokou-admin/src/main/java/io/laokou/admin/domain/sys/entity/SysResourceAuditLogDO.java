package io.laokou.admin.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.laokou.common.entity.BaseDO;
import lombok.Data;

import java.util.Date;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/26 0026 下午 5:31
 */
@Data
@TableName("boot_sys_resource_audit_log")
public class SysResourceAuditLogDO extends BaseDO {

    private Long resourceId;

    private String auditName;

    private Date auditDate;

    private Integer auditStatus;

    private String comment;

}
