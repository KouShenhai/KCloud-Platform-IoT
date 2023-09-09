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
package org.laokou.common.core.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.*;
import org.lionsoul.ip2region.xdb.Searcher;
import java.io.IOException;

/**
 * @author laokou
 */
@Slf4j
public class AddressUtil {

	private static final Searcher SEARCHER;

	private static final String LOCAL_NETWORK_DESC = "内网IP";

	private static final String IGNORE_DESC = "0";

	static {
		try {
			byte[] bytes = ResourceUtil.getResource("ip2region.xdb").getInputStream().readAllBytes();
			SEARCHER = Searcher.newWithBuffer(bytes);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@SneakyThrows
	public static String getRealAddress(String ip) {
		if (IpUtil.internalIp(ip)) {
			return LOCAL_NETWORK_DESC;
		}
		return addressFormat(SEARCHER.search(ip));
	}

	private static String addressFormat(String address) {
		StringBuilder stringBuffer = new StringBuilder();
		String[] info = address.split("\\|");
		for (String str : info) {
			str = IGNORE_DESC.equals(str) ? "" : str + " ";
			stringBuffer.append(str);
		}
		return stringBuffer.toString().trim();
	}

}
