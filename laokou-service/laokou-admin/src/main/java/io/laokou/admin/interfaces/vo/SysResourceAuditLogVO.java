package io.laokou.admin.interfaces.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/26 0026 下午 5:41
 */
@Data
public class SysResourceAuditLogVO {

    private Long id;
    private String auditName;
    private Date auditDate;
    private Integer auditStatus;
    private String comment;

}
