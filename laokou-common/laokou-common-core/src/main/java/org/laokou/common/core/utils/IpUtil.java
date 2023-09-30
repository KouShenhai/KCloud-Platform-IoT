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

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.Constant;
import org.laokou.common.i18n.utils.StringUtil;

import static org.laokou.common.i18n.common.Constant.*;

/**
 * IP工具类
 *
 * @author laokou
 */
@Slf4j
public class IpUtil {

	public static String getIpAddr(HttpServletRequest request) {
		if (request == null) {
			return IP_UNKNOWN;
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
		return LOCAL_NETWORK_SEGMENT.equals(ip) ? LOCAL_IP : ip.split(Constant.COMMA)[0];
	}

	public static boolean internalIp(String ip) {
		byte[] addr = textToNumericFormatV4(ip);
		if (null != addr) {
			return internalIp(addr) || LOCAL_IP.equals(ip);
		}
		return false;
	}

	private static boolean conditionNull(String ip) {
		return StringUtil.isEmpty(ip) || IP_UNKNOWN.equalsIgnoreCase(ip);
	}

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
		switch (b0) {
			case section1:
				return true;
			case section2:
				if (b1 >= section3 && b1 <= section4) {
					return true;
				}
			case section5:
				return b1 == section6;
			default:
				return false;
		}
	}

	/**
	 * 将IPv4地址转换成字节
	 * @param text IPv4地址
	 * @return byte 字节
	 */
	public static byte[] textToNumericFormatV4(String text) {
		if (text.length() == 0) {
			return null;
		}
		byte[] bytes = new byte[4];
		String[] elements = text.split("\\.", -1);
		try {
			long l;
			int i;
			switch (elements.length) {
				case 1 -> {
					long max = 4294967295L;
					l = Long.parseLong(elements[0]);
					if ((l < 0L) || (l > max)) {
						return null;
					}
					bytes[0] = (byte) (int) (l >> 24 & 0xFF);
					bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
					bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
					bytes[3] = (byte) (int) (l & 0xFF);
				}
				case 2 -> {
					long len3 = 255L;
					l = Integer.parseInt(elements[0]);
					if ((l < 0L) || (l > len3)) {
						return null;
					}
					bytes[0] = (byte) (int) (l & 0xFF);
					l = Integer.parseInt(elements[1]);
					long max2 = 16777215L;
					if ((l < 0L) || (l > max2)) {
						return null;
					}
					bytes[1] = (byte) (int) (l >> 16 & 0xFF);
					bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
					bytes[3] = (byte) (int) (l & 0xFF);
				}
				case 3 -> {
					int len1 = 2;
					for (i = 0; i < len1; ++i) {
						l = Integer.parseInt(elements[i]);
						if ((l < 0L) || (l > 255L)) {
							return null;
						}
						bytes[i] = (byte) (int) (l & 0xFF);
					}
					l = Integer.parseInt(elements[2]);
					long max3 = 65535L;
					if ((l < 0L) || (l > max3)) {
						return null;
					}
					bytes[2] = (byte) (int) (l >> 8 & 0xFF);
					bytes[3] = (byte) (int) (l & 0xFF);
				}
				case 4 -> {
					int len2 = 4;
					for (i = 0; i < len2; ++i) {
						l = Integer.parseInt(elements[i]);
						if ((l < 0L) || (l > 255L)) {
							return null;
						}
						bytes[i] = (byte) (int) (l & 0xFF);
					}
				}
				default -> {
					return null;
				}
			}
		}
		catch (NumberFormatException e) {
			return null;
		}
		return bytes;
	}

}
