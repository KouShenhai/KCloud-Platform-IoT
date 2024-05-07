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

package org.laokou.common.domain.database.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.mybatisplus.mapper.BaseDO;

import static org.laokou.common.i18n.common.DSConstant.BOOT_SYS_DOMAIN_EVENT;

/**
 * @author laokou
 */
@Data
@TableName(BOOT_SYS_DOMAIN_EVENT)
public class DomainEventDO extends BaseDO {

	@Schema(name = "eventType", description = "事件类型")
	private String eventType;

	@Schema(name = "eventStatus", description = "事件状态")
	private String eventStatus;

	@Schema(name = "sourceName", description = "数据源名称")
	private String sourceName;

	@Schema(name = "topic", description = "MQ主题")
	private String topic;

	@Schema(name = "appName", description = "应用名称")
	private String appName;

	@Schema(name = "aggregateId", description = "聚合根ID")
	private Long aggregateId;

	@Schema(name = "attribute", description = "扩展属性")
	private String attribute;

}
