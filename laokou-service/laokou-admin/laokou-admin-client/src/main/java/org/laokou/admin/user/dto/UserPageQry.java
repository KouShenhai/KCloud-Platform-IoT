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

package org.laokou.admin.user.dto;

import lombok.Data;
import org.laokou.common.crypto.utils.AESUtil;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.StringUtil;

/**
 * 分页查询用户命令.
 *
 * @author laokou
 */
@Data
public class UserPageQry extends PageQuery {

	private String username;

	private String mobile;

	private String mail;

	private Integer status;

	private Integer superAdmin;

	public void setUsername(String username) throws Exception {
		this.username = StringUtil.like(AESUtil.encrypt(StringUtil.trim(username)));
	}

	public void setMobile(String mobile) throws Exception {
		this.mobile = StringUtil.like(AESUtil.encrypt(StringUtil.trim(mobile)));
	}

	public void setMail(String mail) throws Exception {
		this.mail = StringUtil.like(AESUtil.encrypt(StringUtil.trim(mail)));
	}

}
