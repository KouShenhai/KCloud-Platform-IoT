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

package org.laokou.admin.noticeLog.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import lombok.Data;
import org.laokou.common.i18n.dto.DTO;

/**
 * @author laokou
 */
@Data
public class NoticeLogExcel extends DTO {

	/**
	 * 标识.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "标识", index = 0)
	private String code;

	/**
	 * 名称.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "名称", index = 1)
	private String name;

	/**
	 * 状态.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "状态", index = 2)
	private String status;

	/**
	 * 参数.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "参数", index = 3)
	private String param;

	/**
	 * 错误信息.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "错误信息", index = 4)
	private String errorMessage;

	/**
	 * 创建时间.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "创建时间", index = 5)
	private String createTime;

}
