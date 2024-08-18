// @formatter:off
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

package ${packageName}.${instanceName}.command;

import lombok.RequiredArgsConstructor;
import ${packageName}.${instanceName}.dto.${className}ModifyCmd;
import org.springframework.stereotype.Component;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.core.utils.SpringContextUtil;
import ${packageName}.${instanceName}.dto.domainevent.Modify${className}Event;

/**
 * @author ${author}
 */
@Component
@RequiredArgsConstructor
public class ${className}ModifyCmdExe {

	private final DomainEventPublisher domainEventPublisher;

	private final SpringContextUtil springContextUtil;

	public void executeVoid(${className}ModifyCmd cmd) {
		// 校验参数

		// 创建修改${comment}事件
		Modify${className}Event modify${className}Event = new Modify${className}Event();
		modify${className}Event.create(LAOKOU_${(className)?upper_case}_TOPIC, cmd.getTag(), ${(className)?upper_case}, springContextUtil.getAppName(),
		domainEventPublisher.publishToCreate(modify${className}Event);
	}

}
// @formatter:on
