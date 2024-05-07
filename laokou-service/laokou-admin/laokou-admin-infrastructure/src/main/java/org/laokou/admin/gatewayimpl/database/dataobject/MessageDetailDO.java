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

package org.laokou.admin.gatewayimpl.database.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import org.laokou.common.mybatisplus.mapper.BaseDO;
import lombok.Data;

import java.io.Serial;

/**
 * @author laokou
 */
@Data
@TableName("boot_sys_message_detail")
@Schema(name = "MessageDetailDO", description = "消息详情")
public class MessageDetailDO extends BaseDO {

	@Serial
	private static final long serialVersionUID = 7760123944108929889L;

	@Schema(name = "messageId", description = "消息ID")
	private Long messageId;

	@Schema(name = "userId", description = "用户ID")
	private Long userId;

	@Schema(name = "readFlag", description = "消息已读标识 0未读 1已读")
	private Integer readFlag;

}
