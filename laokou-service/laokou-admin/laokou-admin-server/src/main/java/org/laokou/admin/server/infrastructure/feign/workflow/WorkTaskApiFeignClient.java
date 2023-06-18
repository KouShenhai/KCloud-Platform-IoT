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
package org.laokou.admin.server.infrastructure.feign.workflow;
import org.laokou.admin.server.infrastructure.feign.workflow.factory.WorkTaskApiFeignClientFallbackFactory;
import org.laokou.common.core.constant.Constant;
import org.laokou.common.core.constant.ServiceConstant;
import org.laokou.common.i18n.core.HttpResult;
import org.laokou.flowable.client.dto.*;
import org.laokou.flowable.client.vo.AssigneeVO;
import org.laokou.flowable.client.vo.PageVO;
import org.laokou.flowable.client.vo.TaskVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
/**
 * @author laokou
 */
@FeignClient(contextId = "workTask",value = Constant.HTTPS_PROTOCOL + ServiceConstant.LAOKOU_FLOWABLE,path = "/work/task/api", fallbackFactory = WorkTaskApiFeignClientFallbackFactory.class)
@Service
public interface WorkTaskApiFeignClient {

    /**
     * 查询任务
     * @param dto
     * @return
     */
    @PostMapping(value = "/query")
    HttpResult<PageVO<TaskVO>> query(@RequestBody TaskDTO dto);

    /**
     * 审批任务
     * @param dto
     * @return
     */
    @PostMapping(value = "/audit")
    HttpResult<AssigneeVO> audit(@RequestBody AuditDTO dto);

    /**
     * 开始任务
     * @param dto
     * @return
     */
    @PostMapping(value = "/start")
    HttpResult<AssigneeVO> start(@RequestBody ProcessDTO dto);

    /**
     * 流程图
     * @param processInstanceId
     * @return
     */
    @GetMapping(value = "/diagram")
    HttpResult<String> diagram(@RequestParam("processInstanceId")String processInstanceId);

    /**
     * 任务委派
     * @param dto
     * @return
     */
    @PostMapping("/delegate")
    HttpResult<AssigneeVO> delegate(@RequestBody DelegateDTO dto);

    /**
     * 任务转办
     * @param dto
     * @return
     */
    @PostMapping("/transfer")
    HttpResult<AssigneeVO> transfer(@RequestBody TransferDTO dto);

    /**
     * 任务处理
     * @param dto
     * @return
     */
    @PostMapping(value = "/resolve")
    HttpResult<AssigneeVO> resolve(@RequestBody ResolveDTO dto);

}
