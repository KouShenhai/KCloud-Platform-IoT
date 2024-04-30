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

package org.laokou.admin.gatewayimpl.database.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.mybatisplus.mapper.BaseDO;

import java.io.Serial;

/**
 * @author laokou
 */
@Data
@TableName("boot_sys_oss_log")
@Schema(name = "OssLogDO", description = "OSS日志")
public class OssLogDO extends BaseDO {

	@Serial
	private static final long serialVersionUID = -5956984981904163817L;

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

	@Schema(name = "eventId", description = "事件ID")
	private Long eventId;

}
