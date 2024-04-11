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

package org.laokou.common.i18n.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.ObjectUtil;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author laokou
 */
@Data
@NoArgsConstructor
@Schema(name = "PageQuery", description = "分页查询参数")
public class PageQuery extends Query {

	@Serial
	private static final long serialVersionUID = 6412915892334241813L;

	/**
	 * 分页参数.
	 */
	public static final String PAGE_QUERY = "pageQuery";

	@Min(1)
	@Schema(name = "pageNum", description = "页码")
	private Integer pageNum = 1;

	@Schema(name = "pageSize", description = "条数")
	@Min(1)
	private Integer pageSize = 10;

	@Schema(name = "pageIndex", description = "索引")
	private Integer pageIndex;

	@Schema(name = "sqlFilter", description = "SQL拼接")
	private String sqlFilter;

	@Schema(name = "startTime", description = "开始时间")
	private String startTime;

	@Schema(name = "endTime", description = "结束时间")
	private String endTime;

	@Schema(name = "ignore", description = "忽略数据权限")
	private boolean ignore;

	@Schema(name = "lastId", description = "上一次ID，可以用于深度分页")
	private Long lastId;

	public PageQuery(Integer pageNum, Integer pageSize) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	public PageQuery ignore() {
		this.ignore = true;
		return this;
	}

	public PageQuery time() {
		if (ObjectUtil.isNull(this.startTime)) {
			throw new SystemException("开始时间不能为空");
		}
		if (ObjectUtil.isNull(this.endTime)) {
			throw new SystemException("结束时间不能为空");
		}
		LocalDateTime startDate = DateUtil.parseTime(startTime, DateUtil.YYYY_ROD_MM_ROD_DD_SPACE_HH_RISK_HH_RISK_SS);
		LocalDateTime endDate = DateUtil.parseTime(endTime, DateUtil.YYYY_ROD_MM_ROD_DD_SPACE_HH_RISK_HH_RISK_SS);
		LocalDateTime minDate = LocalDateTime.of(2021, 12, 31, 23, 59, 59);
		LocalDateTime maxDate = LocalDateTime.of(2100, 1, 1, 0, 0, 0);
		if (DateUtil.isAfter(startDate, endDate)) {
			throw new SystemException("结束时间必须大于开始时间");
		}
		if (DateUtil.isBefore(startDate, minDate) || DateUtil.isAfter(endDate, maxDate)) {
			throw new SystemException("开始时间和结束时间只允许在2022-01-01 ~ 2099-12-31范围之内");
		}
		return this;
	}

	public PageQuery page() {
		this.pageIndex = (pageNum - 1) * pageSize;
		return this;
	}

}
