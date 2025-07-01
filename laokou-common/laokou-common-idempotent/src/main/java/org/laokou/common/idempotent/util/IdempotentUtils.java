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

package org.laokou.common.idempotent.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.RequiredArgsConstructor;
import org.laokou.common.core.util.UUIDGenerator;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 幂等性工具.
 *
 * @author why
 */
@Component
@RequiredArgsConstructor
public class IdempotentUtils {

	private static final ThreadLocal<Boolean> IS_IDEMPOTENT_LOCAL = new TransmittableThreadLocal<>();

	private static final ThreadLocal<Map<String, String>> REQUEST_ID_LOCAL = TransmittableThreadLocal
		.withInitial(HashMap::new);

	public static Map<String, String> getRequestId() {
		return REQUEST_ID_LOCAL.get();
	}

	/**
	 * 判断幂等接口.
	 */
	public static boolean isIdempotent() {
		Boolean status = IS_IDEMPOTENT_LOCAL.get();
		return ObjectUtils.equals(Boolean.TRUE, status);
	}

	/**
	 * 设置接口幂等 扩展方法: 用于开启子线程后设置子线程的幂等性状态, 以及定时任务等.
	 */
	public static void openIdempotent() {
		IS_IDEMPOTENT_LOCAL.set(Boolean.TRUE);
	}

	/**
	 * 清理 不被servlet管理的和开启子线程的需要手动清理.
	 */
	public static void cleanIdempotent() {
		IS_IDEMPOTENT_LOCAL.remove();
		REQUEST_ID_LOCAL.remove();
	}

	/**
	 * 得到幂等键.
	 * @return {@link String }
	 */
	public String getIdempotentKey() {
		return UUIDGenerator.generateUUID();
	}

}
