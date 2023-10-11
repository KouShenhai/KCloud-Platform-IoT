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

package org.laokou.admin.command.user.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.user.OnlineUserListQry;
import org.laokou.admin.dto.user.clientobject.UserOnlineCO;
import org.laokou.auth.domain.user.User;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.jasypt.utils.AesUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class OnlineUserListQryExe {

	private final RedisUtil redisUtil;

	public Result<Datas<UserOnlineCO>> execute(OnlineUserListQry qry) {
		return Result.of(getDatas(qry));
	}

	private String getUserInfoPatternKey() {
		return RedisKeyUtil.getUserInfoKey("*");
	}

	private Set<String> getKeys() {
		return redisUtil.keys(getUserInfoPatternKey());
	}

	private String getUserKeyPrefix() {
		return RedisKeyUtil.getUserInfoKey("");
	}

	private Datas<UserOnlineCO> getDatas(OnlineUserListQry qry) {
		Set<String> keys = getKeys();
		String keyword = qry.getUsername();
		Integer pageNum = qry.getPageNum();
		Integer pageSize = qry.getPageSize();
		List<UserOnlineCO> list = new ArrayList<>(keys.size());
		String userInfoKeyPrefix = getUserKeyPrefix();
		for (String key : keys) {
			User user = (User) redisUtil.get(key);
			String username = AesUtil.decrypt(user.getUsername());
			if (StringUtil.isEmpty(keyword) || username.contains(keyword)) {
				UserOnlineCO co = new UserOnlineCO();
				co.setUsername(username);
				co.setToken(key.substring(userInfoKeyPrefix.length()));
				co.setLoginIp(user.getLoginIp());
				co.setLoginDate(user.getLoginDate());
				list.add(co);
			}
		}
		return new Datas<>(list.size(), list.stream().skip((long) (pageNum - 1) * pageSize).limit(pageSize).toList(),
				null);
	}

}
