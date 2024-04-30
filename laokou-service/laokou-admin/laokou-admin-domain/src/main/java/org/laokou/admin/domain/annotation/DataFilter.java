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

package org.laokou.admin.domain.annotation;

import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

/**
 * @author laokou
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Schema(name = "DataFilter", description = "数据权限注解")
public @interface DataFilter {

	@Schema(name = "tableAlias", description = "数据表别名")
	String tableAlias();

	@Schema(name = "creator", description = "创建人")
	String creator() default "creator";

	@Schema(name = "deptPath", description = "部门PATH")
	String deptPath() default "dept_path";

}
