/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.domain.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.admin.domain.oss.OssLog;
import org.laokou.common.core.context.UserContextHolder;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.common.constants.EventType;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.i18n.utils.DateUtil;

import static org.laokou.common.i18n.common.constants.EventStatus.CREATED;
import static org.laokou.common.i18n.common.RocketMqConstant.LAOKOU_FILE_UPLOAD_EVENT_TOPIC;

/**
 * @author laokou
 */
@Data
@NoArgsConstructor
@Schema(name = "FileUploadEvent", description = "文件上传事件")
public class FileUploadEvent extends DomainEvent<Long> {

	@Schema(name = "md5", description = "文件的MD5标识")
	private String md5;

	@Schema(name = "url", description = "文件的URL")
	private String url;

	@Schema(name = "name", description = "文件名称")
	private String name;

	@Schema(name = "size", description = "文件大小")
	private Long size;

	@Schema(name = "status", description = "操作状态 0成功 1失败")
	private Integer status;

	@Schema(name = "errorMessage", description = "错误信息")
	private String errorMessage;

	public FileUploadEvent(OssLog ossLog, UserContextHolder.User user, String appName, Integer status,
			EventType eventType) {
		super(IdGenerator.defaultSnowflakeId(), user.getId(), eventType, CREATED, LAOKOU_FILE_UPLOAD_EVENT_TOPIC,
				user.getSourceName(), appName, user.getId(), user.getId(), user.getDeptId(), user.getDeptPath(),
				user.getTenantId(), DateUtil.now(), DateUtil.now());
		this.errorMessage = ossLog.getErrorMessage();
		this.name = ossLog.getName();
		this.md5 = ossLog.getMd5();
		this.url = ossLog.getUrl();
		this.size = ossLog.getSize();
		this.status = status;
	}

}
