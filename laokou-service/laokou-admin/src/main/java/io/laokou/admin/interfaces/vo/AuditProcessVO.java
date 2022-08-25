package io.laokou.admin.interfaces.vo;

import liquibase.pro.packaged.U;
import lombok.Data;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/25 0025 上午 10:32
 */
@Data
public class AuditProcessVO {

    private String businessKey;

    private String executionId;

    private String instanceName;

    private String definitionId;

}
