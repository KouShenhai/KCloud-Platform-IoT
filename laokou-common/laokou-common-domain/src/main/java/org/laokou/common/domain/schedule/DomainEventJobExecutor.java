/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
 *
 */

package org.laokou.common.domain.schedule;

import com.aizuda.snailjob.client.job.core.annotation.JobExecutor;
import com.aizuda.snailjob.client.job.core.dto.JobArgs;
import com.aizuda.snailjob.client.model.ExecuteResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.domain.service.DomainEventService;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DomainEventJobExecutor {

	private final DomainEventService domainEventService;

	@JobExecutor(name = "deleteDomainEventJobExecutor")
	public ExecuteResult jobExecute(JobArgs jobArgs) {
		String param = jobArgs.getJobParams().toString();
		try {
			domainEventService.removeOldByAppNameOfThreeMonths(param);
			return ExecuteResult.success();
		}
		catch (Exception e) {
			log.error("参数为{}，定时执行删除前三个月的领域事件任务失败，错误信息：{}", param, e.getMessage(), e);
			return ExecuteResult.failure();
		}
	}

}
