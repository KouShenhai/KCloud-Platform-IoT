/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
