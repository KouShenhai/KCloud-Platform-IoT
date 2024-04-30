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

package org.laokou.admin.dto.oss.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

/**
 * @author laokou
 */
@Data
@Schema(name = "FileCO", description = "文件")
public class FileCO extends ClientObject {

	@Schema(name = "url", description = "文件的URL")
	private String url;

	@Schema(name = "md5", description = "文件的MD5标识")
	private String md5;

	public FileCO(String url, String md5) {
		this.url = url;
		this.md5 = md5;
	}

}
