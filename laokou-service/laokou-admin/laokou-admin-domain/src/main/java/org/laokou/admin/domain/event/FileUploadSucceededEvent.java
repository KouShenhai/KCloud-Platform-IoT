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
import org.laokou.admin.domain.oss.OssLog;
import org.laokou.common.core.context.UserContextHolder;

import static org.laokou.common.i18n.common.constants.EventType.FILE_UPLOAD_SUCCEEDED;

/**
 * @author laokou
 */
@Data
@Schema(name = "FileLogSucceededEvent", description = "文件上传成功事件")
public class FileUploadSucceededEvent extends FileUploadEvent {

	public FileUploadSucceededEvent(OssLog ossLog, UserContextHolder.User user, String appName) {
		super(ossLog, user, appName, 0, FILE_UPLOAD_SUCCEEDED);
	}

}
