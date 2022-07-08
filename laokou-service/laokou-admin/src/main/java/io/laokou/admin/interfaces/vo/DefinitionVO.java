package io.laokou.admin.interfaces.vo;
import lombok.Data;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/7 0007 上午 8:56
 */
@Data
public class DefinitionVO {

    /**
     * 流程定义id
     */
    private String definitionId;

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 流程key
     */
    private String processKey;

    /**
     * 部署id
     */
    private String deploymentId;

    /**
     * 流程定义状态 1激活 2中止
     */
    private Boolean suspended;

}
