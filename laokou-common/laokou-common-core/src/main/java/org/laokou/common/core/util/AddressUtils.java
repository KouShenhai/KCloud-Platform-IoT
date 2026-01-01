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

package org.laokou.common.core.util;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ResourceExtUtils;
import org.lionsoul.ip2region.service.Config;
import org.lionsoul.ip2region.service.InvalidConfigException;
import org.lionsoul.ip2region.service.Ip2Region;
import org.lionsoul.ip2region.xdb.InetAddressException;
import org.lionsoul.ip2region.xdb.XdbException;

import java.io.IOException;
import java.util.Arrays;

/**
 * IP所属位置解析.
 *
 * @author laokou
 */
@Slf4j
public final class AddressUtils {

	/**
	 * IP搜索器.
	 */
	private static Ip2Region IP_REGION = null;

	static {
		try {
			Config v4Config = Config.custom()
				// 指定缓存策略: NoCache / VIndexCache / BufferCache
				.setCachePolicy(Config.VIndexCache)
				// 设置初始化的查询器数量
				.setSearchers(15)
				.setXdbFile(ResourceExtUtils.getResource("ip2region_v4.xdb").getFile())
				// 设置初始化的查询器数量
				.asV4();
			Config v6Config = Config.custom()
				// 指定缓存策略: NoCache / VIndexCache / BufferCache
				.setCachePolicy(Config.VIndexCache)
				// 设置初始化的查询器数量
				.setSearchers(15)
				.setXdbFile(ResourceExtUtils.getResource("ip2region_v6.xdb").getFile())
				// 设置初始化的查询器数量
				.asV6();
			IP_REGION = Ip2Region.create(v4Config, v6Config);
		}
		catch (IOException | InvalidConfigException | XdbException ex) {
			log.error("Ip2region加载失败，错误信息：{}", ex.getMessage(), ex);
			throw new RuntimeException(ex);
		}
		finally {
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				if (ObjectUtils.isNotNull(IP_REGION)) {
					try {
						IP_REGION.close();
					}
					catch (InterruptedException ex) {
						throw new RuntimeException(ex);
					}
				}
			}));
		}
	}

	private AddressUtils() {
	}

	/**
	 * 根据IP获取所属位置.
	 * @param ip IP
	 * @return 所属位置
	 */
	public static String getRealAddress(String ip) throws InetAddressException, IOException, InterruptedException {
		return IpUtils.internalIp(ip) ? "内网IP" : addressFormat(IP_REGION.search(ip));
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
			.forEach(str -> stringBuilder
				.append(ObjectUtils.equals("0", str) ? StringConstants.EMPTY : str + StringConstants.SPACE));
		return stringBuilder.toString().trim();
	}

}
