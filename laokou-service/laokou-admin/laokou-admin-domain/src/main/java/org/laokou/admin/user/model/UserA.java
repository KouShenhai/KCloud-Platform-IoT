/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import org.laokou.admin.user.model.entity.UserE;
import org.laokou.admin.user.model.enums.OperateType;
import org.laokou.admin.user.model.enums.SuperAdmin;
import org.laokou.admin.user.model.validator.UserParamValidator;
import org.laokou.common.crypto.util.AESUtils;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.common.IdGenerator;
import org.laokou.common.i18n.common.ValidateName;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.common.enums.BizType;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.StringExtUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户领域对象【聚合根】.
 *
 * @author laokou
 */
@Entity
@Getter
public class UserA extends AggregateRoot implements ValidateName {

	private UserE userE;

	/**
	 * 用户操作类型.
	 */
	private OperateType operateType;

	private final IdGenerator commonIdGenerator;

	private final UserParamValidator saveUserParamValidator;

	private final UserParamValidator modifyUserParamValidator;

	private final UserParamValidator resetUserPwdParamValidator;

	private final UserParamValidator modifyUserAuthorityParamValidator;

	private final PasswordEncoder passwordEncoder;

	public UserA(@Qualifier("commonIdGenerator") IdGenerator commonIdGenerator,
			@Qualifier("saveUserParamValidator") UserParamValidator saveUserParamValidator,
			@Qualifier("modifyUserParamValidator") UserParamValidator modifyUserParamValidator,
			@Qualifier("resetUserPwdParamValidator") UserParamValidator resetUserPwdParamValidator,
			@Qualifier("modifyUserAuthorityParamValidator") UserParamValidator modifyUserAuthorityParamValidator,
			PasswordEncoder passwordEncoder) {
		this.commonIdGenerator = commonIdGenerator;
		this.saveUserParamValidator = saveUserParamValidator;
		this.modifyUserParamValidator = modifyUserParamValidator;
		this.resetUserPwdParamValidator = resetUserPwdParamValidator;
		this.modifyUserAuthorityParamValidator = modifyUserAuthorityParamValidator;
		this.passwordEncoder = passwordEncoder;
	}

	public void checkUserParam() throws Exception {
		switch (operateType) {
			case SAVE -> saveUserParamValidator.validateUser(this);
			case MODIFY -> modifyUserParamValidator.validateUser(this);
			case RESET_PWD -> resetUserPwdParamValidator.validateUser(this);
			case MODIFY_AUTHORITY -> modifyUserAuthorityParamValidator.validateUser(this);
			default -> throw new UnsupportedOperationException("Unsupported operation");
		}
	}

	public UserA create(UserE userE, OperateType operateType) {
		this.userE = userE;
		Long primaryKey = this.userE.getId();
		super.createTime = InstantUtils.now();
		super.id = ObjectUtils.isNotNull(primaryKey) ? primaryKey : commonIdGenerator.getId(BizType.USER);
		this.operateType = operateType;
		return this;
	}

	public UserA encryptByCreate() throws Exception {
		String username = this.userE.getUsername();
		String mail = this.userE.getMail();
		String mobile = this.userE.getMobile();
		this.userE = this.userE.toBuilder()
			.superAdmin(SuperAdmin.NO.getCode())
			.username(AESUtils.encrypt(username))
			.usernamePhrase(encryptPhrase(username))
			.password(encodedPassword("laokou123"))
			.mail(AESUtils.encrypt(mail))
			.mobile(AESUtils.encrypt(mobile))
			.mobilePhrase(encryptMobile(mobile))
			.mailPhrase(encryptPhrase(mail))
			.build();
		return this;
	}

	public UserA encryptByUpdate() throws Exception {
		String mail = this.userE.getMail();
		String mobile = this.userE.getMobile();
		this.userE = this.userE.toBuilder()
			.username(null)
			.usernamePhrase(null)
			.mobile(AESUtils.encrypt(mobile))
			.mobilePhrase(encryptMobile(mobile))
			.mailPhrase(encryptPhrase(mail))
			.password(null)
			.mail(AESUtils.encrypt(mail))
			.build();
		return this;
	}

	public UserA encryptByRestPwd() {
		this.userE = this.userE.toBuilder()
			.username(null)
			.usernamePhrase(null)
			.mobile(null)
			.mobilePhrase(null)
			.mailPhrase(null)
			.password(encodedPassword(this.userE.getPassword()))
			.mail(null)
			.build();
		return this;
	}

	public List<Long> createBatchUserRoleIds(int num) {
		return commonIdGenerator.getIds(BizType.USER, num);
	}

	public boolean isSave() {
		return ObjectUtils.equals(OperateType.SAVE, this.operateType);
	}

	public boolean isModify() {
		return ObjectUtils.equals(OperateType.MODIFY, this.operateType);
	}

	@Override
	public String getValidateName() {
		return "User";
	}

	private String encryptMobile(String mobile) throws Exception {
		if (StringExtUtils.isEmpty(mobile)) {
			return StringConstants.EMPTY;
		}
		List<String> list = new ArrayList<>(3);
		list.add(AESUtils.encrypt(mobile.substring(0, 3)));
		list.add(AESUtils.encrypt(mobile.substring(3, 7)));
		list.add(AESUtils.encrypt(mobile.substring(7)));
		return StringExtUtils.collectionToDelimitedString(list, "~");
	}

	private String encryptPhrase(String phrase) throws Exception {
		if (StringExtUtils.isEmpty(phrase)) {
			return StringConstants.EMPTY;
		}
		List<String> list = new ArrayList<>(30);
		for (int i = 0; i <= phrase.length() - 4; i++) {
			list.add(AESUtils.encrypt(phrase.substring(i, i + 4)));
		}
		return StringExtUtils.collectionToDelimitedString(list, "~");
	}

	private String encodedPassword(String password) {
		if (StringExtUtils.isEmpty(password)) {
			return null;
		}
		return passwordEncoder.encode(password);
	}

	public Instant getCreateTime() {
		return isSave() ? super.createTime : null;
	}

}
