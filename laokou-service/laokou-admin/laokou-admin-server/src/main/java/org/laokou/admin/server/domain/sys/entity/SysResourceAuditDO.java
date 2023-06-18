/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
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
import lombok.Data;
import org.laokou.common.mybatisplus.entity.BaseDO;

import java.io.Serial;

/**
 * @author laokou
 */
@Data
@TableName("boot_sys_resource_audit")
@Schema(name = "SysResourceAuditDO", description = "系统资源审批实体类")
public class SysResourceAuditDO extends BaseDO {

	@Serial
	private static final long serialVersionUID = -2335690532963777527L;

	/**
	 * 资源编号
	 */
	@Schema(name = "resourceId", description = "资源编号")
	private Long resourceId;

	/**
	 * 资源标题
	 */
	@Schema(name = "title", description = "资源标题")
	private String title;

	/**
	 * 资源URL
	 */
	@Schema(name = "url", description = "资源URL")
	private String url;

	/**
	 * 资源编码
	 */
	@Schema(name = "code", description = "资源编码")
	private String code;

	/**
	 * 资源备注
	 */
	@Schema(name = "remark", description = "资源备注")
	private String remark;

	/**
	 * 流程id
	 */
	@Schema(name = "processInstanceId", description = "流程id")
	private String processInstanceId;

	/**
	 * 资源状态 0 待审核 1 审核中 2 审批驳回 3 审批通过
	 */
	@Schema(name = "status", description = "资源状态 0 待审核 1 审核中 2 审批驳回 3 审批通过")
	private Integer status;

}
