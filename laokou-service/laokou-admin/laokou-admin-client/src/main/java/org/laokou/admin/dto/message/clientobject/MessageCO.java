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

package org.laokou.admin.dto.message.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

import java.time.Instant;
import java.util.Set;

/**
 * @author laokou
 */
@Data
@Schema(name = "MessageCO", description = "消息")
public class MessageCO extends ClientObject {

	@Schema(name = "id", description = "ID")
	private Long id;

	@Schema(name = "title", description = "消息标题")
	private String title;

	@Schema(name = "content", description = "消息内容")
	private String content;

	@Schema(name = "createDate", description = "创建时间")
	private Instant createDate;

	@Schema(name = "type", description = "消息类型 0通知 1提醒")
	private Integer type;

	@NotEmpty(message = "接收人集合不为空")
	@Schema(name = "receiver", description = "接收人集合")
	private Set<String> receiver;

}
