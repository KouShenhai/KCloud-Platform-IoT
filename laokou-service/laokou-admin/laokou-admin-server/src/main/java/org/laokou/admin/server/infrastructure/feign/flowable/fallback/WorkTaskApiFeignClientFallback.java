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
package org.laokou.admin.server.infrastructure.feign.flowable.fallback;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.server.infrastructure.feign.flowable.WorkTaskApiFeignClient;
import org.laokou.common.i18n.core.HttpResult;
import org.laokou.flowable.client.dto.AuditDTO;
import org.laokou.flowable.client.dto.ProcessDTO;
import org.laokou.flowable.client.dto.TaskDTO;
import org.laokou.flowable.client.vo.AssigneeVO;
import org.laokou.flowable.client.vo.PageVO;
import org.laokou.flowable.client.vo.TaskVO;
/**
 * 服务降级
 * @author laokou
 */
@Slf4j
@AllArgsConstructor
public class WorkTaskApiFeignClientFallback implements WorkTaskApiFeignClient {

    private final Throwable throwable;

    @Override
    public HttpResult<PageVO<TaskVO>> query(TaskDTO dto) {
        log.error("流程查询失败，报错原因：{}",throwable.getMessage());
        return new HttpResult<PageVO<TaskVO>>().ok(new PageVO<>());
    }

    @Override
    public HttpResult<AssigneeVO> audit(AuditDTO dto) {
        log.error("流程审批失败，报错原因：{}",throwable.getMessage());
        return new HttpResult<AssigneeVO>().error("流程审批失败，请联系管理员");
    }

    @Override
    public HttpResult<AssigneeVO> start(ProcessDTO dto) {
        log.error("未启动流程，报错原因：{}",throwable.getMessage());
        return new HttpResult<AssigneeVO>().error("未启动流程，请联系管理员");
    }

    @Override
    public HttpResult<String> diagram(String processInstanceId) {
        log.error("流程图查看失败，报错原因：{}",throwable.getMessage());
        return new HttpResult<String>().error("流程图查看失败，请联系管理员");
    }

}
