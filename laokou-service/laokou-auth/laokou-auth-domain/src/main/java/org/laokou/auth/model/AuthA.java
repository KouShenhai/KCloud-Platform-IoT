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

package org.laokou.auth.model;

import lombok.Getter;
import org.laokou.auth.ability.validator.CaptchaValidator;
import org.laokou.auth.ability.validator.PasswordValidator;
import org.laokou.auth.dto.domainevent.LoginEvent;
import org.laokou.common.i18n.common.exception.GlobalException;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static org.laokou.auth.model.GrantType.*;
import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;
import static org.laokou.common.i18n.common.exception.StatusCode.FORBIDDEN;
import static org.laokou.common.i18n.common.exception.SystemException.OAuth2.*;

/**
 * 认证聚合.
 *
 * @author laokou
 */
@Getter
public class AuthA extends AggregateRoot {

	/**
	 * 业务用例.
	 */
	public static final String USE_CASE_AUTH = "auth";

	/**
	 * 用户名.
	 */
	private final String username;

	/**
	 * 密码.
	 */
	private final String password;

	/**
	 * 租户编号.
	 */
	private final String tenantCode;

	/**
	 * 认证类型 mail邮箱 mobile手机号 password密码 authorization_code授权码.
	 */
	private final GrantType grantType;

	/**
	 * 验证码值对象.
	 */
	private final CaptchaV captcha;

	/**
	 * 用户实体.
	 */
	private UserE user;

	/**
	 * 数据源前缀.
	 */
	private String sourcePrefix;

	/**
	 * 菜单权限标识.
	 */
	private Set<String> permissions;

	/**
	 * 部门路径.
	 */
	private Set<String> deptPaths;

	/**
	 * 扩展信息.
	 */
	private InfoV info;

	public AuthA(Long aggregateId, String username, String password, String tenantCode, GrantType grantType,
			String uuid, String captcha) {
		super.id = aggregateId;
		this.username = username;
		this.password = password;
		this.tenantCode = tenantCode;
		this.grantType = grantType;
		this.captcha = new CaptchaV(uuid, captcha);
	}

	public void createUserByPassword() {
		this.user = new UserE(this.username, EMPTY, EMPTY);
	}

	public void createUserByMobile() {
		this.user = new UserE(EMPTY, EMPTY, this.captcha.uuid());
	}

	public void createUserByMail() {
		this.user = new UserE(EMPTY, this.captcha.uuid(), EMPTY);
	}

	public void createUserByAuthorizationCode() {
		this.user = new UserE(this.username, EMPTY, EMPTY);
	}

	public void getExtInfo(InfoV info) {
		this.info = info;
	}

	public void getTenantId(Long tenantId) {
		super.tenantId = tenantId;
	}

	public void getSourcePrefix(String sourcePrefix) {
		this.sourcePrefix = sourcePrefix;
	}

	public void getUserInfo(UserE user) {
		this.user = user;
		super.userId = ObjectUtil.isNotNull(this.user) ? this.user.getId() : null;
	}

	public void getMenuPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public void getDeptPaths(List<String> deptPaths) {
		this.deptPaths = getPaths(deptPaths);
	}

	public void checkTenantId() {
		if (ObjectUtil.isNull(super.tenantId)) {
			throw new SystemException(TENANT_NOT_EXIST);
		}
	}

	public void checkCaptcha(CaptchaValidator captchaValidator) {
		if (isUseCaptcha()) {
			Boolean validate = captchaValidator.validate(captcha.uuid(), captcha.captcha());
			if (ObjectUtil.isNull(validate)) {
				throw new SystemException(CAPTCHA_EXPIRED);
			}
			if (!validate) {
				throw new SystemException(CAPTCHA_ERROR);
			}
		}
	}

	public void checkSourcePrefix() {
		if (ObjectUtil.isNull(sourcePrefix)) {
			throw new SystemException(DATA_SOURCE_NOT_EXIST);
		}
	}

	public void checkUsername() {
		if (ObjectUtil.isNull(this.user)) {
			this.grantType.checkUsernameNotExist();
		}
	}

	public void checkPassword(PasswordValidator passwordValidator) {
		if (PASSWORD.equals(this.grantType) && !passwordValidator.validate(this.password, user.getPassword())) {
			throw new SystemException(USERNAME_PASSWORD_ERROR);
		}
	}

	public void checkUserStatus() {
		if (ObjectUtil.equals(UserStatus.DISABLE.ordinal(), this.user.getStatus())) {
			throw new SystemException(USER_DISABLED);
		}
	}

	public void checkMenuPermissions() {
		if (CollectionUtils.isEmpty(this.permissions)) {
			throw new SystemException(FORBIDDEN);
		}
	}

	public void checkDeptPaths() {
		if (CollectionUtils.isEmpty(this.deptPaths)) {
			throw new SystemException(FORBIDDEN);
		}
	}

	public void recordLog(Long eventId, GlobalException e) {
		addEvent(new DomainEvent(eventId, null, null, null, null, null, super.version, null));
		super.version++;
	}

	private boolean isUseCaptcha() {
		return List.of(PASSWORD, MOBILE, MAIL).contains(grantType);
	}

	private Set<String> getPaths(List<String> list) {
		if (CollectionUtils.isEmpty(list)) {
			return Collections.emptySet();
		}
		// 字符串长度排序
		list.sort(Comparator.comparingInt(String::length));
		Set<String> paths = new HashSet<>(list.size());
		paths.add(list.getFirst());
		for (String path : list.subList(1, list.size())) {
			int find = paths.size();
			for (String p : paths) {
				if (path.contains(p)) {
					break;
				}
				find--;
			}
			if (find == 0) {
				paths.add(path);
			}
		}
		return paths;
	}

	private String getLoginName() {
		if (List.of(PASSWORD, AUTHORIZATION_CODE).contains(grantType)) {
			return this.username;
		}
		return this.captcha.uuid();
	}

	private String getLoginType() {
		return this.grantType.getCode();
	}

	private LoginEvent getEvent(GlobalException e) {
		if (ObjectUtil.isNull(e)) {
			return null;
		}
		if (e instanceof SystemException) {
			int status = LoginStatus.FAIL.ordinal();
			String errorMessage = e.getMessage();
		}
		return null;
	}

}
