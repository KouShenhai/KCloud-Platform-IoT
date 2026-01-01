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

package org.laokou.common.ratelimiter.aspectj;

import jakarta.servlet.http.HttpServletRequest;
import org.laokou.common.context.util.UserUtils;
import org.laokou.common.core.util.IpUtils;
import org.laokou.common.i18n.common.constant.StringConstants;

/**
 * 类型枚举.
 *
 * @author laokou
 */
public enum Type {

	/**
	 * 默认.
	 */
	DEFAULT {
		@Override
		public String resolve(HttpServletRequest request) {
			return StringConstants.EMPTY;
		}
	},

	/**
	 * IP.
	 */
	IP {
		@Override
		public String resolve(HttpServletRequest request) {
			return IpUtils.getIpAddr(request);
		}
	},

	/**
	 * 路径.
	 */
	PATH {
		@Override
		public String resolve(HttpServletRequest request) {
			return request.getContextPath();
		}
	},

	/**
	 * 用户.
	 */
	USER {
		@Override
		public String resolve(HttpServletRequest request) {
			return UserUtils.getUserId().toString();
		}
	},

	/**
	 * 租户.
	 */
	TENANT {
		@Override
		public String resolve(HttpServletRequest request) {
			return UserUtils.getTenantId().toString();
		}
	};

	public abstract String resolve(HttpServletRequest request);

}
