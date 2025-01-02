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

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;

import static org.laokou.common.i18n.common.constant.StringConstant.COMMA;

/**
 * IP工具类.
 *
 * @author laokou
 */
@Slf4j
public final class IpUtil {

	/**
	 * 本地IP-IPV4.
	 */
	public static final String LOCAL_IPV4 = "127.0.0.1";

	/**
	 * 未知IP.
	 */
	private static final String UNKNOWN_IP = "unknown";

	/**
	 * 本地IP-IPV6.
	 */
	private static final String LOCAL_IPV6 = "0:0:0:0:0:0:0:1";

	private IpUtil() {
	}

	/**
	 * 获取IP地址.
	 * @param request 请求对象
	 * @return IP地址
	 */
	public static String getIpAddr(HttpServletRequest request) {
		if (ObjectUtil.isNull(request)) {
			return UNKNOWN_IP;
		}
		String ip = request.getHeader("x-forwarded-for");
		if (conditionNull(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (conditionNull(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (conditionNull(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (conditionNull(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (conditionNull(ip)) {
			ip = request.getRemoteAddr();
		}
		return LOCAL_IPV6.equals(ip) || StringUtil.isEmpty(ip) ? LOCAL_IPV4 : ip.split(COMMA)[0];
	}

	/**
	 * 判断是否内部IP.
	 * @param ip IP地址
	 * @return 判断结果
	 */
	public static boolean internalIp(String ip) {
		if (LOCAL_IPV6.equals(ip)) {
			return true;
		}
		byte[] bytes = textToNumericFormatV4(ip);
		return bytes.length > 0 && (internalIp(bytes) || LOCAL_IPV4.equals(ip));
	}

	/**
	 * 将IPv4地址转换成字节.
	 * @param text IPv4地址
	 * @return 字节
	 */
	private static byte[] textToNumericFormatV4(String text) {
		if (text.isEmpty()) {
			return new byte[0];
		}
		byte[] bytes = new byte[4];
		String[] elements = text.split("\\.", -1);
		try {
			long l;
			long j;
			switch (elements.length) {
				case 1:
					l = Long.parseLong(elements[0]);
					j = 4294967295L;
					if ((l < 0L) || (l > j)) {
						return new byte[0];
					}
					bytes[0] = (byte) (int) (l >> 24 & 0xFF);
					bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
					bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
					bytes[3] = (byte) (int) (l & 0xFF);
					break;
				case 2:
					l = Integer.parseInt(elements[0]);
					j = 255;
					if (l < 0L || l > j) {
						return new byte[0];
					}
					bytes[0] = (byte) (int) (l & 0xFF);
					l = Integer.parseInt(elements[1]);
					j = 16777215;
					if (l < 0L || l > j) {
						return new byte[0];
					}
					bytes[1] = (byte) (int) (l >> 16 & 0xFF);
					bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
					bytes[3] = (byte) (int) (l & 0xFF);
					break;
				case 3:
					j = 2;
					for (int i = 0; i < j; i++) {
						l = Integer.parseInt(elements[i]);
						if ((l < 0L) || (l > 255L)) {
							return new byte[0];
						}
						bytes[i] = (byte) (int) (l & 0xFF);
					}
					l = Integer.parseInt(elements[2]);
					j = 65535L;
					if ((l < 0L) || (l > j)) {
						return new byte[0];
					}
					bytes[2] = (byte) (int) (l >> 8 & 0xFF);
					bytes[3] = (byte) (int) (l & 0xFF);
					break;
				case 4:
					j = 4;
					for (int i = 0; i < j; i++) {
						l = Integer.parseInt(elements[i]);
						if ((l < 0L) || (l > 255L)) {
							return new byte[0];
						}
						bytes[i] = (byte) (int) (l & 0xFF);
					}
					break;
				default:
					return new byte[0];
			}
		}
		catch (NumberFormatException e) {
			log.error("格式化失败，错误信息：{}", e.getMessage());
			return new byte[0];
		}
		return bytes;
	}

	/**
	 * 判断IP不存在或未知.
	 * @param ip IP地址
	 * @return 判断结果
	 */
	private static boolean conditionNull(String ip) {
		return StringUtil.isEmpty(ip) || UNKNOWN_IP.equalsIgnoreCase(ip);
	}

	/**
	 * 判断内部IP.
	 * @param addr 字节数组
	 * @return 判断结果
	 */
	private static boolean internalIp(byte[] addr) {
		final byte b0 = addr[0];
		final byte b1 = addr[1];
		// 10.x.x.x/8
		final byte section1 = 0x0A;
		// 172.16.x.x/12
		final byte section2 = (byte) 0xAC;
		final byte section3 = (byte) 0x10;
		final byte section4 = (byte) 0x1F;
		// 192.168.x.x/16
		final byte section5 = (byte) 0xC0;
		final byte section6 = (byte) 0xA8;
		return switch (b0) {
			case section1 -> true;
			case section2 -> b1 >= section3 && b1 <= section4;
			case section5 -> b1 == section6;
			default -> false;
		};
	}

}
