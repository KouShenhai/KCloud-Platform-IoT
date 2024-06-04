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
import lombok.Data;
import org.laokou.common.mybatisplus.mapper.BaseDO;

import java.io.Serial;

import static org.laokou.common.i18n.common.DSConstant.BOOT_SYS_DICT_TYPE;

/**
 * @author laokou
 */
@Data
@TableName(BOOT_SYS_DICT_TYPE)
@Schema(name = "DictTypeDO", description = "字典类型")
public class DictTypeDO extends BaseDO {

	@Serial
	private static final long serialVersionUID = 956432385619473630L;

	@Schema(name = "name", description = "字典名称")
	private String name;

	@Schema(name = "type", description = "字典类型")
	private String type;

	@Schema(name = "status", description = "字典状态 0启用 1停用")
	private Integer status;

	@Schema(name = "remark", description = "字典备注")
	private String remark;

}
