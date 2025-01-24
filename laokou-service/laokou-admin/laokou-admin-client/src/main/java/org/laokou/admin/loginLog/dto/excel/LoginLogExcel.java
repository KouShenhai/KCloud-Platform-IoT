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

package org.laokou.admin.loginLog.dto.excel;

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
public class LoginLogExcel extends DTO {

	/**
	 * 登录的用户名.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "用户名称", index = 0)
	private String username;

	/**
	 * 登录的IP地址.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "登录地址", index = 1)
	private String ip;

	/**
	 * 登录的归属地.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "登录地点", index = 2)
	private String address;

	/**
	 * 登录的浏览器.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "浏览器", index = 3)
	private String browser;

	/**
	 * 登录的操作系统.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "操作系统", index = 4)
	private String os;

	/**
	 * 登录状态 0登录成功 1登录失败.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "登录状态", index = 5)
	private String status;

	/**
	 * 错误信息.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "错误信息", index = 6)
	private String errorMessage;

	/**
	 * 登录类型.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "登录类型", index = 7)
	private String type;

	/**
	 * 登录时间.
	 */
	@ColumnWidth(30)
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "登录时间", index = 8)
	private String createTime;

}
