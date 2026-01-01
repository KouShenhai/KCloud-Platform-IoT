/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.auth.gatewayimpl.database.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.laokou.common.mybatisplus.mapper.BaseDO;
import org.laokou.common.tenant.constant.DSConstants;

/**
 * @author laokou
 */
@Data
@TableName(DSConstants.Master.OSS_LOG_TABLE)
public class OssLogDO extends BaseDO {

	/**
	 * 文件名称.
	 */
	private String name;

	/**
	 * 文件的MD5标识.
	 */
	private String md5;

	/**
	 * 文件的URL.
	 */
	private String url;

	/**
	 * 文件大小.
	 */
	private Long size;

	/**
	 * OSS存储ID.
	 */
	private Long ossId;

	/**
	 * 文件类型.
	 */
	private String contentType;

	/**
	 * 文件格式.
	 */
	private String format;

}
