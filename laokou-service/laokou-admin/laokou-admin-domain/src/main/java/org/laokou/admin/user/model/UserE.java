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

import lombok.Getter;
import lombok.Setter;
import org.laokou.common.crypto.util.AESUtils;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.dto.Identifier;
import org.laokou.common.i18n.util.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

import static org.laokou.common.i18n.common.constant.StringConstants.EMPTY;

/**
 * 用户领域对象【实体】.
 *
 * @author laokou
 */
@Entity
public class UserE extends Identifier {

	/**
	 * 用户密码.
	 */
	@Getter
	@Setter
	private String password;

	/**
	 * 超级管理员标识 0否 1是.
	 */
	@Getter
	@Setter
	private Integer superAdmin;

	/**
	 * 用户邮箱.
	 */
	@Getter
	@Setter
	private String mail;

	/**
	 * 用户手机号.
	 */
	@Getter
	@Setter
	private String mobile;

	/**
	 * 用户状态 0启用 1禁用.
	 */
	@Getter
	@Setter
	private Integer status;

	/**
	 * 用户头像.
	 */
	@Getter
	@Setter
	private String avatar;

	/**
	 * 用户名短语.
	 */
	@Getter
	private String usernamePhrase;

	/**
	 * 用户邮箱短语.
	 */
	@Getter
	private String mailPhrase;

	/**
	 * 用户手机号短语.
	 */
	@Getter
	private String mobilePhrase;

	/**
	 * 用户名.
	 */
	@Getter
	@Setter
	private String username;

	/**
	 * 角色IDS.
	 */
	@Getter
	@Setter
	private List<String> roleIds;

	/**
	 * 部门IDS.
	 */
	@Getter
	@Setter
	private List<String> deptIds;

	/**
	 * 用户角色IDS.
	 */
	@Getter
	@Setter
	private List<Long> userRoleIds;

	/**
	 * 用户部门IDS.
	 */
	@Getter
	@Setter
	private List<Long> userDeptIds;

	/**
	 * 用户IDS.
	 */
	@Getter
	@Setter
	private List<Long> userIds;

	/**
	 * 用户操作类型.
	 */
	@Setter
	private UserOperateTypeEnum userOperateTypeEnum;

	private final UserParamValidator saveUserParamValidator;

	private final UserParamValidator modifyUserParamValidator;

	private final UserParamValidator resetUserPwdParamValidator;

	private final UserParamValidator modifyUserAuthorityParamValidator;

	public UserE(@Qualifier("saveUserParamValidator") UserParamValidator saveUserParamValidator,
			@Qualifier("modifyUserParamValidator") UserParamValidator modifyUserParamValidator,
			@Qualifier("resetUserPwdParamValidator") UserParamValidator resetUserPwdParamValidator,
			@Qualifier("modifyUserAuthorityParamValidator") UserParamValidator modifyUserAuthorityParamValidator) {
		this.saveUserParamValidator = saveUserParamValidator;
		this.modifyUserParamValidator = modifyUserParamValidator;
		this.resetUserPwdParamValidator = resetUserPwdParamValidator;
		this.modifyUserAuthorityParamValidator = modifyUserAuthorityParamValidator;
	}

	public void checkUserParam() throws Exception {
		switch (userOperateTypeEnum) {
			case SAVE -> saveUserParamValidator.validateUser(this);
			case MODIFY -> modifyUserParamValidator.validateUser(this);
			case RESET_PWD -> resetUserPwdParamValidator.validateUser(this);
			case MODIFY_AUTHORITY -> modifyUserAuthorityParamValidator.validateUser(this);
			default -> {
			}
		}
	}

	public void encryptUsername() throws Exception {
		this.usernamePhrase = StringUtils.isEmpty(username) ? EMPTY : encryptStr(username);
		this.username = AESUtils.encrypt(username);
	}

	public void encryptMail() throws Exception {
		this.mailPhrase = StringUtils.isEmpty(mail) ? EMPTY : encryptStr(mail);
		this.mail = AESUtils.encrypt(mail);
	}

	public void encryptMobile() throws Exception {
		this.mobilePhrase = StringUtils.isEmpty(mobile) ? EMPTY : encryptMobile(mobile);
		this.mobile = AESUtils.encrypt(mobile);
	}

	private String encryptMobile(String str) throws Exception {
		List<String> list = new ArrayList<>(3);
		list.add(AESUtils.encrypt(str.substring(0, 3)));
		list.add(AESUtils.encrypt(str.substring(3, 7)));
		list.add(AESUtils.encrypt(str.substring(7)));
		return StringUtils.collectionToDelimitedString(list, "~");
	}

	private String encryptStr(String str) throws Exception {
		List<String> list = new ArrayList<>(30);
		for (int i = 0; i <= str.length() - 4; i++) {
			list.add(AESUtils.encrypt(str.substring(i, i + 4)));
		}
		return StringUtils.collectionToDelimitedString(list, "~");
	}

}
