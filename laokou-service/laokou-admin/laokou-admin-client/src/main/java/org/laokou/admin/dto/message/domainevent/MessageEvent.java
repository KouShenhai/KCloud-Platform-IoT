/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.dto.message.domainevent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.laokou.common.i18n.dto.DomainEvent;

import java.util.Set;

/**
 * @author laokou
 */
@Getter
@Setter
@Schema(name = "MessageEvent", description = "消息事件")
public class MessageEvent extends DomainEvent {

	@Schema(name = "title", description = "消息标题")
	private String title;

	@Schema(name = "content", description = "消息内容")
	private String content;

	@Schema(name = "type", description = "消息类型 0通知 1提醒")
	private Integer type;

	@Schema(name = "receiver", description = "接收人集合")
	private Set<String> receiver;

	@Schema(name = "instanceId", description = "实例ID")
	private String instanceId;

	public MessageEvent(Object source) {
		super(source);
	}

}
