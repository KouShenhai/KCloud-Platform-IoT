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

package org.laokou.common.core.utils;

import lombok.SneakyThrows;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.ResourceUtil;
import org.lionsoul.ip2region.xdb.Searcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.laokou.common.i18n.common.constant.StringConstant.*;

/**
 * IP所属位置解析.
 *
 * @author laokou
 */
public final class AddressUtil {

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
		try (InputStream inputStream = ResourceUtil.getResource("ip2region.xdb").getInputStream()) {
			SEARCHER = Searcher.newWithBuffer(inputStream.readAllBytes());
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private AddressUtil() {
	}

	/**
	 * 根据IP获取所属位置.
	 * @param ip IP
	 * @return 所属位置
	 */
	@SneakyThrows
	public static String getRealAddress(String ip) {
		return IpUtil.internalIp(ip) ? LOCAL_ADDR : addressFormat(SEARCHER.search(ip));
	}

	/**
	 * 位置格式化.
	 * @param address 所属位置
	 * @return 格式化后的位置
	 */
	private static String addressFormat(String address) {
		StringBuilder stringBuilder = new StringBuilder(address.length());
		String[] info = address.split(BACKSLASH + ERECT);
		Arrays.stream(info)
			.forEach(str -> stringBuilder.append(ObjectUtil.equals(EMPTY_ADDR, str) ? EMPTY : str + SPACE));
		return stringBuilder.toString().trim();
	}

}
