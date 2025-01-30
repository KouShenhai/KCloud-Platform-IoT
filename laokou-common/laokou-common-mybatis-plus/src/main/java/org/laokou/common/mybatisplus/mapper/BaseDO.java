/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.mybatisplus.mapper;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * 基类映射.
 *
 * @author laokou
 */
@Data
public abstract class BaseDO implements Serializable {

	/**
	 * 创建人。
	 */
	public static final String CREATOR = "creator";

	/**
	 * 编辑人.
	 */
	public static final String EDITOR = "editor";

	/**
	 * 创建时间.
	 */
	public static final String CREATE_TIME = "createTime";

	/**
	 * 修改时间.
	 */
	public static final String UPDATE_TIME = "updateTime";

	/**
	 * 删除标识.
	 */
	public static final String DEL_FLAG = "delFlag";

	/**
	 * 版本.
	 */
	public static final String VERSION = "version";

	/**
	 * 租户ID.
	 */
	public static final String TENANT_ID = "tenantId";

	@Serial
	private static final long serialVersionUID = -5855413730985647400L;

	/**
	 * ID.
	 */
	@TableId(type = IdType.INPUT)
	protected Long id;

	/**
	 * 创建人.
	 */
	@TableField(fill = FieldFill.INSERT)
	protected Long creator;

	/**
	 * 编辑人.
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	protected Long editor;

	/**
	 * 创建时间.
	 */
	@TableField(fill = FieldFill.INSERT)
	protected Instant createTime;

	/**
	 * 修改时间.
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	protected Instant updateTime;

	/**
	 * 删除标识 0未删除 1已删除.
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	protected Integer delFlag;

	/**
	 * 版本号.
	 */
	@Version
	@TableField(fill = FieldFill.INSERT)
	protected Integer version;

	/**
	 * 租户ID.
	 */
	@TableField(fill = FieldFill.INSERT)
	protected Long tenantId;

}
