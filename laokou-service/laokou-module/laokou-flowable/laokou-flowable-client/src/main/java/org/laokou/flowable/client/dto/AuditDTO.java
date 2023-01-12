/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.flowable.client.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author laokou
 */
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

    /**
     * 业务key
     */
    private String businessKey;

    /**
     * 实例名称
     */
    private String instanceName;

    /**
     * 实例id
     */
    private String definitionId;

    /**
     * 流程变量
     */
    private Map<String,Object> values;

}
