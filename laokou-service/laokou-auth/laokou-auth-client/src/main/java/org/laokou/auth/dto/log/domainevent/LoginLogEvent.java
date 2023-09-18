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
package org.laokou.auth.dto.log.domainevent;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;
import java.io.Serializable;
import java.time.Clock;

/**
 * @author laokou
 */
@Setter
@Getter
public class LoginLogEvent extends ApplicationEvent implements Serializable {

	@Serial
	private static final long serialVersionUID = -325094951800650353L;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 登录用户
	 */
	private String username;

	/**
	 * ip地址
	 */
	private String ip;

	/**
	 * 操作地点
	 */
	private String address;

	/**
	 * 浏览器
	 */
	private String browser;

	/**
	 * 操作系统
	 */
	private String os;

	/**
	 * 状态 0：成功 1：失败
	 */
	private Integer status;

	/**
	 * 提示信息
	 */
	private String message;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 租户ID
	 */
	private Long tenantId;

	/**
	 * 部门ID
	 */
	private Long deptId;

	/**
	 * 部门PATH
	 */
	private String deptPath;

	public LoginLogEvent(Object source) {
		super(source);
	}

	public LoginLogEvent(Object source, Clock clock) {
		super(source, clock);
	}

}
