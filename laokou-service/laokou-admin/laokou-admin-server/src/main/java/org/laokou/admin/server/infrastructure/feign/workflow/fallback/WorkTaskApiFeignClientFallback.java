/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 *//*
	 *
	 * package org.laokou.admin.server.infrastructure.feign.workflow.fallback;
	 *
	 * import lombok.AllArgsConstructor; import lombok.extern.slf4j.Slf4j; import
	 * org.laokou.admin.server.infrastructure.feign.workflow.WorkTaskApiFeignClient;
	 * import org.laokou.common.i18n.dto.Result; import org.laokou.flowable.client.dto.*;
	 * import org.laokou.flowable.client.vo.AssigneeVO; import
	 * org.laokou.flowable.client.vo.PageVO; import org.laokou.flowable.client.vo.TaskVO;
	 *
	 */
/**
 * 服务降级
 *
 * @author laokou
 *//*
	 *
	 * @Slf4j
	 *
	 * @AllArgsConstructor public class WorkTaskApiFeignClientFallback implements
	 * WorkTaskApiFeignClient {
	 *
	 * private final Throwable throwable;
	 *
	 * @Override public Result<PageVO<TaskVO>> query(TaskDTO dto) {
	 * log.error("流程查询失败，报错原因：{}", throwable.getMessage()); return Result.of(new
	 * PageVO<>()); }
	 *
	 * @Override public Result<AssigneeVO> audit(AuditDTO dto) {
	 * log.error("流程审批失败，报错原因：{}", throwable.getMessage()); return
	 * Result.fail("流程审批失败，请联系管理员"); }
	 *
	 * @Override public Result<AssigneeVO> start(ProcessDTO dto) {
	 * log.error("流程启动失败，报错原因：{}", throwable.getMessage()); return
	 * Result.fail("流程启动失败，请联系管理员"); }
	 *
	 * @Override public Result<String> diagram(String processInstanceId) {
	 * log.error("流程图查看失败，报错原因：{}", throwable.getMessage()); return
	 * Result.fail("流程图查看失败，请联系管理员"); }
	 *
	 * @Override public Result<AssigneeVO> delegate(DelegateDTO dto) {
	 * log.error("流程委派失败，报错原因：{}", throwable.getMessage()); return
	 * Result.fail("流程委派失败，请联系管理员"); }
	 *
	 * @Override public Result<AssigneeVO> transfer(TransferDTO dto) {
	 * log.error("流程转办失败，报错原因：{}", throwable.getMessage()); return
	 * Result.fail("流程转办失败，请联系管理员"); }
	 *
	 * @Override public Result<AssigneeVO> resolve(ResolveDTO dto) {
	 * log.error("流程处理失败，报错原因：{}", throwable.getMessage()); return
	 * Result.fail("流程处理失败，请联系管理员"); }
	 *
	 * }
	 */
