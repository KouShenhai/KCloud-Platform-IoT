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

package org.laokou.common.i18n.common;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author laokou
 */
@Schema(name = "MybatisPlusConstants", description = "MybatisPlus常量")
public final class MybatisPlusConstants {

	private MybatisPlusConstants() {
	}

	@Schema(name = "PLACE_HOLDER", description = "分表标识符")
	public static final String PLACE_HOLDER = "$$";

	@Schema(name = "PRIMARY_KEY", description = "ID")
	public static final String PRIMARY_KEY = "id";

	@Schema(name = "CREATOR", description = "创建人")
	public static final String CREATOR = "creator";

	@Schema(name = "EDITOR", description = "编辑人")
	public static final String EDITOR = "editor";

	@Schema(name = "CREATE_DATE", description = "创建时间")
	public static final String CREATE_DATE = "createDate";

	@Schema(name = "UPDATE_DATE", description = "修改时间")
	public static final String UPDATE_DATE = "updateDate";

	@Schema(name = "DEL_FLAG", description = "删除标识")
	public static final String DEL_FLAG = "delFlag";

	@Schema(name = "VERSION", description = "版本")
	public static final String VERSION = "version";

	@Schema(name = "DEPT_ID", description = "部门ID")
	public static final String DEPT_ID = "deptId";

	@Schema(name = "DEPT_PATH", description = "部门PATH")
	public static final String DEPT_PATH = "deptPath";

	@Schema(name = "TENANT_ID", description = "租户ID")
	public static final String TENANT_ID = "tenantId";

	@Schema(name = "USER_ID", description = "用户ID")
	public static final String USER_ID = "userId";

}
