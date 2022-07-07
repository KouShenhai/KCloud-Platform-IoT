package io.laokou.admin.interfaces.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/7 0007 下午 5:34
 */
@Data
public class TaskVO {

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务key
     */
    private String taskDefinitionKey;

    /**
     * 任务执行人名称
     */
    private String assigneeName;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 流程变量
     */
    private Object processVariables;

}
