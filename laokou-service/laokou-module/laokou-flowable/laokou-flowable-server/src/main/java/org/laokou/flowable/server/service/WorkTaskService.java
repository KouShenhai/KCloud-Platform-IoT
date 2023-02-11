/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.flowable.server.service;
import jakarta.servlet.http.HttpServletResponse;
import org.laokou.flowable.client.dto.AuditDTO;
import org.laokou.flowable.client.dto.ProcessDTO;
import org.laokou.flowable.client.dto.TaskDTO;
import org.laokou.flowable.client.vo.AssigneeVO;
import org.laokou.flowable.client.vo.PageVO;
import org.laokou.flowable.client.vo.TaskVO;
import java.io.IOException;

/**
 * @author laokou
 */
public interface WorkTaskService {

    /**
     * 审批任务
     * @param dto
     * @return
     */
    AssigneeVO auditTask(AuditDTO dto);

    /**
     * 开始任务
     * @param dto
     * @return
     */
    AssigneeVO startTask(ProcessDTO dto);

    /**
     * 任务分页
     * @param dto
     * @return
     */
    PageVO<TaskVO> queryTaskPage(TaskDTO dto);

    /**
     * 任务流程图
     * @param processInstanceId
     * @param response
     * @throws IOException
     */
    void diagramTask(String processInstanceId, HttpServletResponse response) throws IOException;

}
