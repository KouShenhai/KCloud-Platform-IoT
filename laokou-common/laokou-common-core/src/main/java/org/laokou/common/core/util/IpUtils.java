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

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.StringExtUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * IP 工具类.
 *
 * @author laokou
 */
@Slf4j
public final class IpUtils {

	/**
	 * 本地IP-IPV4.
	 */
	public static final String LOCAL_IPV4 = "127.0.0.1";

	/**
	 * 本地IP-IPV6.
	 */
	public static final String LOCAL_IPV6 = "0:0:0:0:0:0:0:1";

	/**
	 * 未知IP.
	 */
	public static final String UNKNOWN_IP = "unknown";

	/**
	 * 常见代理 Header（按优先级）.
	 */
	private static final List<String> IP_HEADER_CANDIDATES = List.of("X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP",
			"WL-Proxy-Client-IP");

	private IpUtils() {
	}

	/**
	 * 获取客户端真实 IP.
	 */
	public static String getIpAddr(HttpServletRequest request) {
		if (ObjectUtils.isNull(request)) {
			return UNKNOWN_IP;
		}

		for (String header : IP_HEADER_CANDIDATES) {
			String ip = request.getHeader(header);
			if (isValidIpStr(ip)) {
				return parseClientIp(ip);
			}
		}
		String remoteAddr = request.getRemoteAddr();
		return normalizeLocalIp(remoteAddr);
	}

	/**
	 * 判断是否为内网 IP（支持 IPv4 / IPv6）.
	 */
	public static boolean internalIp(String ip) {
		if (!isValidIpStr(ip)) {
			throw new BizException("B_Network_IpAddrInvited", "无效IP地址");
		}
		try {
			InetAddress address = InetAddress.getByName(ip);
			return address.isLoopbackAddress() || address.isSiteLocalAddress();
		}
		catch (UnknownHostException e) {
			log.warn("Invalid IP address: {}", ip);
			return false;
		}
	}

	/**
	 * 解析 X-Forwarded-For 中的第一个 IP.
	 */
	private static String parseClientIp(String ip) {
		int commaIndex = ip.indexOf(',');
		String clientIp = (commaIndex > 0) ? ip.substring(0, commaIndex).trim() : ip;
		return normalizeLocalIp(clientIp);
	}

	/**
	 * 本地 IP 归一化.
	 */
	private static String normalizeLocalIp(String ip) {
		if (StringExtUtils.isEmpty(ip) || LOCAL_IPV6.equals(ip)) {
			return LOCAL_IPV4;
		}
		return ip;
	}

	/**
	 * 判断 IP 是否有效.
	 */
	private static boolean isValidIpStr(String ip) {
		if (StringExtUtils.isEmpty(ip) || UNKNOWN_IP.equalsIgnoreCase(ip)) {
			return false;
		}
		int commaIndex = ip.indexOf(',');
		if (commaIndex > 0) {
			String[] strs = ip.split(",");
			for (String str : strs) {
				if (!isValidIp(str.trim())) {
					return false;
				}
			}
			return true;
		}
		else {
			return isValidIp(ip);
		}
	}

	private static boolean isValidIp(String ip) {
		return RegexUtils.ipv4Regex(ip) || RegexUtils.ipv6Regex(ip);
	}

}
