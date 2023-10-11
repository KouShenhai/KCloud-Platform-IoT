/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.utils.DateUtil;
import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author laokou
 */
@Data
@NoArgsConstructor
@Schema(name = "PageQuery", description = "分页")
public class PageQuery extends Query {

	@Serial
	private static final long serialVersionUID = 6412915892334241813L;

	public static final String PAGE_QUERY = "pageQuery";

	@NotNull(message = "显示页码不为空")
	@Min(value = 1)
	@Schema(name = "pageNum", description = "页码")
	private Integer pageNum;

	@NotNull(message = "显示条数不为空")
	@Schema(name = "pageSize", description = "条数")
	@Min(value = 1)
	private Integer pageSize;

	@Schema(name = "pageIndex", description = "索引")
	private Integer pageIndex;

	@Schema(name = "sqlFilter", description = "SQL拼接")
	private String sqlFilter;

	@Schema(name = "startTime", description = "开始时间")
	private String startTime;

	@Schema(name = "endTime", description = "结束时间")
	private String endTime;

	@Schema(name = "ignore", description = "是否忽略")
	private boolean ignore;

	@Schema(name = "lastId", description = "上一次ID，用于深度分页")
	private Long lastId;

	public PageQuery(Integer pageNum, Integer pageSize) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	public PageQuery ignore(boolean ignore) {
		this.ignore = ignore;
		return this;
	}

	public PageQuery time() {
		if (this.startTime == null) {
			throw new GlobalException("开始时间不为空");
		}
		if (this.endTime == null) {
			throw new GlobalException("结束时间不为空");
		}
		int yearOfDays = 730;
		LocalDateTime startDate = DateUtil.parseTime(startTime, DateUtil.YYYY_MM_DD_HH_MM_SS);
		LocalDateTime endDate = DateUtil.parseTime(endTime, DateUtil.YYYY_MM_DD_HH_MM_SS);
		LocalDateTime minDate = LocalDateTime.of(2021, 12, 31, 23, 59, 59);
		LocalDateTime maxDate = LocalDateTime.of(2100, 1, 1, 0, 0, 0);
		if (DateUtil.isAfter(startDate, endDate)) {
			throw new GlobalException("结束时间必须大于开始时间");
		}
		if (DateUtil.getDays(startDate, endDate) > yearOfDays) {
			throw new GlobalException("开始时间和结束时间间隔不能超过两年");
		}
		if (DateUtil.isBefore(startDate, minDate) || DateUtil.isAfter(endDate, maxDate)) {
			throw new GlobalException("开始时间和结束时间只允许在2022-01-01 ~ 2099-12-31范围之内");
		}
		return this;
	}

	public PageQuery page() {
		this.pageIndex = (pageNum - 1) * pageSize;
		return this;
	}

}