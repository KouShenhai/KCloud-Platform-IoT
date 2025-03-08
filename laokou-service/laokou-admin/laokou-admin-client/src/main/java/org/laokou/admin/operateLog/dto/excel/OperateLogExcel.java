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

package org.laokou.admin.operateLog.dto.excel;

import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.write.style.ColumnWidth;
import cn.idev.excel.annotation.write.style.ContentStyle;
import cn.idev.excel.enums.BooleanEnum;
import cn.idev.excel.enums.poi.HorizontalAlignmentEnum;
import cn.idev.excel.enums.poi.VerticalAlignmentEnum;
import lombok.Data;
import org.laokou.common.i18n.dto.DTO;

/**
 * @author laokou
 */
@Data
public class OperateLogExcel extends DTO {

	/**
	 * 操作的模块名称.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "模块名称", index = 0)
	private String moduleName;

	/**
	 * 请求的操作名称.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "操作名称", index = 1)
	private String name;

	/**
	 * 操作的请求类型.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "请求类型", index = 2)
	private String requestType;

	/**
	 * 操作人.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "操作人员", index = 3)
	private String operator;

	/**
	 * 操作的IP地址.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "IP地址", index = 4)
	private String ip;

	/**
	 * 操作的归属地.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "IP归属地", index = 5)
	private String address;

	/**
	 * 操作状态 0成功 1失败.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "状态", index = 6)
	private String status;

	/**
	 * 错误信息.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "错误信息", index = 7)
	private String errorMessage;

	/**
	 * 操作的消耗时间(毫秒).
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "消耗时间(毫秒)", index = 8)
	private Long costTime;

	/**
	 * 创建时间.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "创建时间", index = 9)
	private String createTime;

}
