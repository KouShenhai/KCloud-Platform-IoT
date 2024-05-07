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

package org.laokou.admin.dto.log.clientobject;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author laokou
 */
@Data
@Schema(name = "OperateLogExcel", description = "操作日志")
public class OperateLogExcel implements Serializable {

	@ColumnWidth(40)
	@Schema(name = "moduleName", description = "操作的模块名称")
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "模块名称", index = 0)
	private String moduleName;

	@ColumnWidth(40)
	@Schema(name = "name", description = "操作名称")
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "操作名称", index = 1)
	private String name;

	@ColumnWidth(40)
	@Schema(name = "operator", description = "操作人")
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "操作人员", index = 2)
	private String operator;

	@ColumnWidth(40)
	@Schema(name = "requestType", description = "操作的请求类型")
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "请求方式", index = 3)
	private String requestType;

	@ColumnWidth(40)
	@Schema(name = "uri", description = "操作的URI")
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "请求地址", index = 4)
	private String uri;

	@ColumnWidth(40)
	@Schema(name = "ip", description = "操作的IP地址")
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "主机", index = 5)
	private String ip;

	@ColumnWidth(40)
	@Schema(name = "address", description = "操作的归属地")
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "操作地点", index = 6)
	private String address;

	@ColumnWidth(40)
	@Schema(name = "userAgent", description = "操作的浏览器")
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "浏览器", index = 7)
	private String userAgent;

	@ColumnWidth(40)
	@Schema(name = "methodName", description = "操作的方法名")
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "方法名称", index = 8)
	private String methodName;

	@ColumnWidth(50)
	@Schema(name = "requestParams", description = "操作的请求参数")
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "请求参数", index = 9)
	private String requestParams;

	@ColumnWidth(20)
	@Schema(name = "status", description = "操作状态 0成功 1失败")
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "操作状态", index = 10)
	private Integer status;

	@ColumnWidth(40)
	@Schema(name = "statusDesc", description = "操作状态描述")
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "操作状态描述", index = 11)
	private String statusDesc;

	@ColumnWidth(40)
	@Schema(name = "errorMessage", description = "错误信息")
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "错误信息", index = 12)
	private String errorMessage;

	@ColumnWidth(40)
	@Schema(name = "createDate", description = "创建时间")
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "操作时间", index = 13)
	private LocalDateTime createDate;

	@ColumnWidth(40)
	@Schema(name = "takeTime", description = "操作的消耗时间(毫秒)")
	@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
			verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
	@ExcelProperty(value = "耗时（毫秒）", index = 14)
	private Long takeTime;

	public void setStatus(Integer status) {
		this.status = status;
		if (this.status == 1) {
			this.statusDesc = "操作失败";
		}
		else {
			this.statusDesc = "操作成功";
		}
	}

}
