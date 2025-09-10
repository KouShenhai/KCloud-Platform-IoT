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

package org.laokou.common.core.util;

import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ResourceUtils;
import org.lionsoul.ip2region.xdb.Searcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * IP所属位置解析.
 *
 * @author laokou
 */
public final class AddressUtils {

	/**
	 * 未解析的位置.
	 */
	private static final String EMPTY_ADDR = "0";

	/**
	 * 本地的位置.
	 */
	private static final String LOCAL_ADDR = "内网IP";

	/**
	 * IP搜索器.
	 */
	private static final Searcher SEARCHER;

	static {
		try (InputStream inputStream = ResourceUtils.getResource("ip2region.xdb").getInputStream()) {
			SEARCHER = Searcher.newWithBuffer(inputStream.readAllBytes());
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private AddressUtils() {
	}

	/**
	 * 根据IP获取所属位置.
	 * @param ip IP
	 * @return 所属位置
	 */
	public static String getRealAddress(String ip) throws Exception {
		return IpUtils.internalIp(ip) ? LOCAL_ADDR : addressFormat(SEARCHER.search(ip));
	}

	/**
	 * 位置格式化.
	 * @param address 所属位置
	 * @return 格式化后的位置
	 */
	private static String addressFormat(String address) {
		StringBuilder stringBuilder = new StringBuilder(address.length());
		String[] info = address.split(StringConstants.BACKSLASH + StringConstants.ERECT);
		Arrays.stream(info)
			.forEach(str -> stringBuilder.append(ObjectUtils.equals(EMPTY_ADDR, str) ? StringConstants.EMPTY : str + StringConstants.SPACE));
		return stringBuilder.toString().trim();
	}

}
