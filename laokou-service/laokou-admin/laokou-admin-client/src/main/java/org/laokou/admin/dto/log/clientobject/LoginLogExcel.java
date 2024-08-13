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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author laokou
 */
@Data
@Schema(name = "LoginLogExcel", description = "登录日志")
public class LoginLogExcel implements Serializable {

	@ColumnWidth(30)
	@Schema(name = "username", description = "登录的用户名")
	@ExcelProperty(value = "用户名称", index = 0)
	private String username;

	@ColumnWidth(30)
	@Schema(name = "ip", description = "登录的IP地址")
	@ExcelProperty(value = "登录地址", index = 1)
	private String ip;

	@ColumnWidth(30)
	@Schema(name = "address", description = "登录的归属地")
	@ExcelProperty(value = "登录地点", index = 2)
	private String address;

	@ColumnWidth(30)
	@Schema(name = "browser", description = "登录的浏览器")
	@ExcelProperty(value = "浏览器", index = 3)
	private String browser;

	@ColumnWidth(30)
	@Schema(name = "os", description = "登录的操作系统")
	@ExcelProperty(value = "操作系统", index = 4)
	private String os;

	@ColumnWidth(30)
	@ExcelProperty(value = "登录状态", index = 5)
	@Schema(name = "status", description = "登录状态 0登录成功 1登录失败")
	private Integer status;

	@ColumnWidth(30)
	@Schema(name = "statusDesc", description = "登录状态描述")
	@ExcelProperty(value = "登录状态描述", index = 6)
	private String statusDesc;

	@ColumnWidth(30)
	@Schema(name = "message", description = "登录信息")
	@ExcelProperty(value = "登录信息", index = 7)
	private String message;

	@ColumnWidth(30)
	@Schema(name = "type", description = "登录类型")
	@ExcelProperty(value = "登录类型", index = 8)
	private String type;

	@ColumnWidth(30)
	@Schema(name = "创建时间", description = "创建时间")
	@ExcelProperty(value = "创建时间", index = 9)
	private Instant createTime;

	public void setStatus(Integer status) {
		this.status = status;
		if (this.status == 1) {
			this.statusDesc = "登录失败";
		}
		else {
			this.statusDesc = "登录成功";
		}
	}

}
