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
package io.laokou.admin.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.dto.AuditDTO;
import io.laokou.admin.interfaces.qo.TaskQO;
import io.laokou.admin.interfaces.vo.StartProcessVO;
import io.laokou.admin.interfaces.vo.TaskVO;

import javax.servlet.http.HttpServletRequest;

public interface WorkflowProcessApplicationService {

    StartProcessVO startResourceProcess(String processKey,String businessKey,String instanceName);

    IPage<TaskVO> queryResourceTaskPage(TaskQO qo, HttpServletRequest request);

    Boolean auditResourceTask(AuditDTO dto,HttpServletRequest request);
}
