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

package org.laokou.common.i18n.common;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author laokou
 */
@Schema(name = "ResponseHeaderConstants", description = "响应头常量")
public final class ResponseHeaderConstant {

	private ResponseHeaderConstant() {
	}

	@Schema(name = "EXCEL_CONTENT_TYPE", description = "Excel类型")
	public static final String EXCEL_CONTENT_TYPE = "application/vnd.ms-excel";

	@Schema(name = "CONTENT_DISPOSITION", description = "Content Disposition")
	public static final String CONTENT_DISPOSITION = "Content-disposition";

	@Schema(name = "STREAM_CONTENT_TYPE", description = "Steam类型")
	public static final String STREAM_CONTENT_TYPE = "application/octet-stream";

	@Schema(name = "ACCESS_CONTROL_EXPOSE_HEADERS", description = "Access Control Expose Headers")
	public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

}
