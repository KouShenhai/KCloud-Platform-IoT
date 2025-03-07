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

package org.laokou.admin.user.model;

import lombok.Data;
import org.laokou.common.crypto.utils.AESUtil;
import org.laokou.common.i18n.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;

/**
 * 用户领域对象【实体】.
 *
 * @author laokou
 */
@Data
public class UserE {

	/**
	 * ID.
	 */
	private Long id;

	/**
	 * 密码.
	 */
	private String password;

	/**
	 * 超级管理员标识 0否 1是.
	 */
	private Integer superAdmin;

	/**
	 * 邮箱.
	 */
	private String mail;

	/**
	 * 手机号.
	 */
	private String mobile;

	/**
	 * 用户状态 0启用 1禁用.
	 */
	private Integer status;

	/**
	 * 头像.
	 */
	private String avatar;

	/**
	 * 用户名短语.
	 */
	private String usernamePhrase;

	/**
	 * 邮箱短语.
	 */
	private String mailPhrase;

	/**
	 * 手机号短语.
	 */
	private String mobilePhrase;

	/**
	 * 用户名.
	 */
	private String username;

	/**
	 * 角色IDS.
	 */
	private List<String> roleIds;

	/**
	 * 部门IDS.
	 */
	private List<String> deptIds;

	/**
	 * 用户角色IDS.
	 */
	private List<Long> userRoleIds;

	/**
	 * 用户部门IDS.
	 */
	private List<Long> userDeptIds;

	/**
	 * 用户IDS.
	 */
	private List<Long> userIds;

	public void encryptUsername() throws Exception {
		this.usernamePhrase = StringUtil.isEmpty(username) ? EMPTY : encryptStr(username);
		this.username = AESUtil.encrypt(username);
	}

	public void encryptMail() throws Exception {
		this.mailPhrase = StringUtil.isEmpty(mail) ? EMPTY : encryptStr(mail);
		this.mail = AESUtil.encrypt(mail);
	}

	public void encryptMobile() throws Exception {
		this.mobilePhrase = StringUtil.isEmpty(mobile) ? EMPTY : encryptMobile(mobile);
		this.mobile = AESUtil.encrypt(mobile);
	}

	private String encryptMobile(String str) throws Exception {
		List<String> list = new ArrayList<>(3);
		list.add(AESUtil.encrypt(str.substring(0, 3)));
		list.add(AESUtil.encrypt(str.substring(3, 7)));
		list.add(AESUtil.encrypt(str.substring(7)));
		return StringUtil.collectionToDelimitedString(list, "~");
	}

	private String encryptStr(String str) throws Exception {
		List<String> list = new ArrayList<>(30);
		for (int i = 0; i <= str.length() - 4; i++) {
			list.add(AESUtil.encrypt(str.substring(i, i + 4)));
		}
		return StringUtil.collectionToDelimitedString(list, "~");
	}

}
