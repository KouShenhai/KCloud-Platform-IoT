/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.admin.server.domain.sys.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.laokou.common.mybatisplus.entity.BaseDO;
import lombok.Data;
/**
 * 系统菜单
 * @author laokou
 */
@Data
@TableName("boot_sys_menu")
@Schema(name = "SysMenuDO",description = "系统菜单实体类")
public class SysMenuDO extends BaseDO {

	/**
	 * 父菜单ID，一级菜单为0
	 */
	@NotBlank(message = "{sys.menu.pid.require}")
	@Schema(name = "pid",description = "父菜单ID",example = "0")
	private Long pid;

	/**
	 * 菜单名称
	 */
	@NotBlank(message = "{sys.menu.name.require}")
	@Schema(name = "name",description = "菜单名称",example = "用户管理")
	private String name;

	/**
	 * 菜单URL
	 */
	@Schema(name = "url",description = "菜单URL",example = "/sys/user/api/login")
	private String url;

	/**
	 * 菜单权限标识
	 */
	@Schema(name = "permission",description = "菜单权限标识",example = "sys:user:query")
	private String permission;

	/**
	 * 菜单图标
	 */
	@Schema(name = "icon",description = "菜单图标",example = "user")
	private String icon;

	/**
	 * 字典排序
	 */
	@Schema(name = "sort",description = "字典排序",example = "1")
	private Integer sort;

	/**
	 * 菜单类型 0 菜单 1 按钮
	 */
	@Schema(name = "type",description = "字典类型 0 菜单 1 按钮",example = "0")
	private Integer type;

}
