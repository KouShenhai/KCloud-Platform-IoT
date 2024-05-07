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

package org.laokou.admin.dto.oss;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.admin.dto.oss.clientobject.OssCO;
import org.laokou.common.i18n.dto.CommonCommand;

/**
 * @author laokou
 */
@Data
@Schema(name = "OssInsertCmd", description = "新增OSS命令请求")
public class OssCreateCmd extends CommonCommand {

	@Schema(name = "ossCO", description = "OSS")
	private OssCO ossCO;

}
