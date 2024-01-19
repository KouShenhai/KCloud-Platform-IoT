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

package org.laokou.admin.dto.oss.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

/**
 * @author laokou
 */
@Data
@Schema(name = "", description = "")
public class OssCO extends ClientObject {

	@Schema(name = "", description = "")
	private Long id;

	@Schema(name = "", description = "")
	private String name;

	@Schema(name = "", description = "")
	private String endpoint;

	@Schema(name = "", description = "")
	private String region;

	@Schema(name = "", description = "")
	private String accessKey;

	@Schema(name = "", description = "")
	private String secretKey;

	@Schema(name = "", description = "")
	private String bucketName;

	@Schema(name = "", description = "")
	private Integer pathStyleAccessEnabled;

}
