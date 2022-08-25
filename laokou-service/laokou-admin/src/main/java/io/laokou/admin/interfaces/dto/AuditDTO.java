package io.laokou.admin.interfaces.dto;
import lombok.Data;
import java.util.Map;
@Data
public class AuditDTO {

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务意见
     */
    private String comment;

    /**
     * 流程实例id
     */
    private String instanceId;

    private String businessKey;

    private String instanceName;

    private String definitionId;

    /**
     * 流程变量
     */
    private Map<String,Object> values;

}
