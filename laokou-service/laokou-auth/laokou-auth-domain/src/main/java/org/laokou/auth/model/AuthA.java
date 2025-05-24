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

package org.laokou.auth.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.laokou.auth.ability.validator.CaptchaValidator;
import org.laokou.auth.ability.validator.PasswordValidator;
import org.laokou.auth.dto.domainevent.LoginEvent;
import org.laokou.auth.dto.domainevent.SendCaptchaEvent;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.common.constant.EventTypeEnum;
import org.laokou.common.i18n.common.exception.GlobalException;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static org.laokou.auth.model.GrantTypeEnum.*;
import static org.laokou.auth.model.OAuth2Constants.*;
import static org.laokou.common.i18n.common.constant.EventTypeEnum.LOGIN_EVENT;
import static org.laokou.common.i18n.common.constant.StringConstants.EMPTY;
import static org.laokou.common.i18n.common.exception.StatusCode.FORBIDDEN;

/**
 * 认证聚合.
 *
 * @author laokou
 */
@Getter
@Entity
@NoArgsConstructor
public class AuthA extends AggregateRoot {

	/**
	 * 用户名.
	 */
	private String username;

	/**
	 * 用户密码.
	 */
	private String password;

	/**
	 * 租户编码.
	 */
	private String tenantCode;

	// @formatter:off
	/**
	 * 认证类型.
	 * mail邮箱
	 * mobile手机号
	 * username_password用户名密码
	 * authorization_code授权码
	 * test测试
	 */
	// @formatter:on
	private GrantTypeEnum grantTypeEnum;

	/**
	 * 验证码值对象.
	 */
	private CaptchaV captcha;

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

	/**
	 * 验证码实体.
	 */
	private CaptchaE captchaE;

	/**
	 * 密码校验器.
	 */
	@Setter
	@Autowired
	private PasswordValidator passwordValidator;

	/**
	 * 验证码校验器.
	 */
	@Setter
	@Autowired
	private CaptchaValidator captchaValidator;

	public AuthA fillValue(Long id, String tenantCode) {
		super.id = id;
		this.tenantCode = tenantCode;
		return this;
	}

	public AuthA fillValue(Long id, String username, String password, String tenantCode, GrantTypeEnum grantTypeEnum,
			String uuid, String captcha) {
		super.id = id;
		this.username = username;
		this.password = password;
		this.tenantCode = tenantCode;
		this.grantTypeEnum = grantTypeEnum;
		this.captcha = new CaptchaV(uuid, captcha);
		return this;
	}

	public void createUserByUsernamePassword() throws Exception {
		this.user = DomainFactory.getUser(this.username, EMPTY, EMPTY);
	}

	public void createUserByMobile() throws Exception {
		this.user = DomainFactory.getUser(EMPTY, EMPTY, this.captcha.uuid());
	}

	public void createUserByMail() throws Exception {
		this.user = DomainFactory.getUser(EMPTY, this.captcha.uuid(), EMPTY);
	}

	public void createUserByAuthorizationCode() throws Exception {
		this.user = DomainFactory.getUser(this.username, EMPTY, EMPTY);
	}

	public void createUserByTest() throws Exception {
		this.user = DomainFactory.getUser(this.username, EMPTY, EMPTY);
	}

	public void createCaptcha(Long eventId) {
		addEvent(new DomainEvent(eventId, tenantId, null, super.id, MqEnum.MOBILE_CAPTCHA.getTopic(), captchaE.getTag(),
				super.version, JacksonUtils.toJsonStr(new SendCaptchaEvent(captchaE.getUuid())),
				EventTypeEnum.SEND_CAPTCHA_EVENT, sourcePrefix));
		super.version++;
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
		super.userId = ObjectUtils.isNotNull(this.user) ? this.user.getId() : null;
	}

	public void getMenuPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public void getDeptPaths(List<String> deptPaths) {
		this.deptPaths = getPaths(deptPaths);
	}

	public void getCaptcha(CaptchaE captcha) {
		this.captchaE = captcha;
	}

	public void checkTenantId() {
		if (ObjectUtils.isNull(super.tenantId)) {
			throw new BizException(TENANT_NOT_EXIST);
		}
	}

	public void checkCaptcha() {
		if (isUseCaptcha()) {
			Boolean validate = this.captchaValidator.validateCaptcha(getCaptchaCacheKey(), captcha.captcha());
			if (ObjectUtils.isNull(validate)) {
				throw new BizException(CAPTCHA_EXPIRED);
			}
			if (!validate) {
				throw new BizException(CAPTCHA_ERROR);
			}
		}
	}

	public void checkSourcePrefix() {
		if (ObjectUtils.isNull(sourcePrefix)) {
			throw new BizException(DATA_SOURCE_NOT_EXIST);
		}
	}

	public void checkUsername() {
		if (ObjectUtils.isNull(this.user)) {
			this.grantTypeEnum.checkUsernameNotExist();
		}
	}

	public void checkPassword() {
		if (isUsePassword() && !this.passwordValidator.validatePassword(this.password, user.getPassword())) {
			throw new BizException(USERNAME_PASSWORD_ERROR);
		}
	}

	public void checkUserStatus() {
		if (ObjectUtils.equals(UserStatusEnum.DISABLE.getCode(), this.user.getStatus())) {
			throw new BizException(USER_DISABLED);
		}
	}

	public void checkMenuPermissions() {
		if (CollectionUtils.isEmpty(this.permissions)) {
			throw new BizException(FORBIDDEN);
		}
	}

	public void checkDeptPaths() {
		if (CollectionUtils.isEmpty(this.deptPaths)) {
			throw new BizException(FORBIDDEN);
		}
	}

	public void recordLoginLog(Long eventId, GlobalException e) {
		LoginEvent event = getEvent(e);
		if (ObjectUtils.isNotNull(event)) {
			addEvent(new DomainEvent(eventId, super.tenantId, super.userId, super.id, MqEnum.LOGIN_LOG.getTopic(),
					MqEnum.LOGIN_LOG.getTag(), super.version, JacksonUtils.toJsonStr(event), LOGIN_EVENT,
					sourcePrefix));
			super.version++;
		}
	}

	private boolean isUseCaptcha() {
		return List.of(USERNAME_PASSWORD, MOBILE, MAIL).contains(grantTypeEnum);
	}

	private boolean isUsePassword() {
		return List.of(USERNAME_PASSWORD, AUTHORIZATION_CODE, TEST).contains(grantTypeEnum);
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
		if (List.of(USERNAME_PASSWORD, AUTHORIZATION_CODE, TEST).contains(grantTypeEnum)) {
			return this.username;
		}
		return this.captcha.uuid();
	}

	private LoginEvent getEvent(GlobalException e) {
		if (ObjectUtils.isNull(e)) {
			return new LoginEvent(getLoginName(), info.ip(), info.address(), info.browser(), info.os(),
					LoginStatusEnum.OK.getCode(), EMPTY, grantTypeEnum.getCode(), super.instant);
		}
		else if (e instanceof BizException ex) {
			return new LoginEvent(getLoginName(), info.ip(), info.address(), info.browser(), info.os(),
					LoginStatusEnum.FAIL.getCode(), ex.getMsg(), grantTypeEnum.getCode(), super.instant);
		}
		return null;
	}

	private String getCaptchaCacheKey() {
		return switch (grantTypeEnum) {
			case MOBILE -> RedisKeyUtils.getMobileAuthCaptchaKey(captcha.uuid());
			case MAIL -> RedisKeyUtils.getMailAuthCaptchaKey(captcha.uuid());
			case USERNAME_PASSWORD, AUTHORIZATION_CODE, TEST ->
				RedisKeyUtils.getUsernamePasswordAuthCaptchaKey(captcha.uuid());
		};
	}

}
