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

package org.laokou.common.core;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.AddressUtils;
import org.laokou.common.i18n.common.exception.BizException;
import org.lionsoul.ip2region.xdb.InetAddressException;

import java.io.IOException;

/**
 * @author laokou
 */
class AddressUtilsTest {

	@Test
	void test_getRealAddress_withLocalhost() throws Exception {
		// 测试本地回环地址
		String result = AddressUtils.getRealAddress("127.0.0.1");
		Assertions.assertThat(result).isEqualTo("内网IP");
	}

	@Test
	void test_getRealAddress_withPrivateIp_10() throws Exception {
		// 测试10.x.x.x内网IP
		String result = AddressUtils.getRealAddress("10.0.0.1");
		Assertions.assertThat(result).isEqualTo("内网IP");
	}

	@Test
	void test_getRealAddress_withPrivateIp_172() throws Exception {
		// 测试172.16.x.x-172.31.x.x内网IP
		String result = AddressUtils.getRealAddress("172.16.0.1");
		Assertions.assertThat(result).isEqualTo("内网IP");
		result = AddressUtils.getRealAddress("172.31.255.255");
		Assertions.assertThat(result).isEqualTo("内网IP");
	}

	@Test
	void test_getRealAddress_withPrivateIp_192() throws Exception {
		// 测试192.168.x.x内网IP
		String result = AddressUtils.getRealAddress("192.168.1.1");
		Assertions.assertThat(result).isEqualTo("内网IP");
	}

	@Test
	void test_getRealAddress_withPublicIpv4() throws Exception {
		// 测试公网IPv4地址
		String result = AddressUtils.getRealAddress("8.8.8.8");
		Assertions.assertThat(result).isNotNull().isNotEqualTo("内网IP");
	}

	@Test
	void test_getRealAddress_withPublicIpv6() throws Exception {
		// 测试公网IPv6地址
		String result = AddressUtils.getRealAddress("2001:4860:4860::8888");
		Assertions.assertThat(result).isNotEqualTo("内网IP");
	}

	@Test
	void test_getRealAddress_withInvalidIp() {
		// 测试无效IP地址
		Assertions.assertThatThrownBy(() -> AddressUtils.getRealAddress("invalid.ip"))
			.isExactlyInstanceOf(BizException.class);
	}

	@Test
	void test_getRealAddress_withNull() {
		// 测试null IP地址
		Assertions.assertThatThrownBy(() -> AddressUtils.getRealAddress(null)).isExactlyInstanceOf(BizException.class);
	}

	@Test
	void test_getRealAddress_withEmptyString() {
		// 测试空字符串IP地址
		Assertions.assertThatThrownBy(() -> AddressUtils.getRealAddress("")).isExactlyInstanceOf(BizException.class);
	}

	@Test
	void test_getRealAddress_withMalformedIp() {
		// 测试格式错误的IP地址
		Assertions.assertThatThrownBy(() -> AddressUtils.getRealAddress("256.256.256.256"))
			.isExactlyInstanceOf(BizException.class);

	}

	@Test
	void test_getRealAddress_withShortIp() {
		// 测试不完整的IP地址
		Assertions.assertThatThrownBy(() -> AddressUtils.getRealAddress("192.168"))
			.isExactlyInstanceOf(BizException.class);
	}

	@Test
	void test_getRealAddress_withSpecialCharacters() throws InetAddressException, IOException, InterruptedException {
		// 测试包含特殊字符的IP地址
		Assertions.assertThatThrownBy(() -> AddressUtils.getRealAddress("192.168.1.x"))
			.isExactlyInstanceOf(BizException.class);
	}

	@Test
	void test_getRealAddress_withMultipleDots() throws InetAddressException, IOException, InterruptedException {
		// 测试多个点的IP地址
		Assertions.assertThatThrownBy(() -> AddressUtils.getRealAddress("192.168.1.1.1"))
			.isExactlyInstanceOf(BizException.class);
	}

	@Test
	void test_getRealAddress_withCommonPublicIps() throws Exception {
		// 测试常见公网IP地址
		String[] publicIps = { "114.114.114.114", "223.5.5.5", "180.76.76.76" };
		for (String ip : publicIps) {
			String result = AddressUtils.getRealAddress(ip);
			Assertions.assertThat(result).isNotNull().isNotEqualTo("内网IP");
		}
	}

	@Test
	void test_getRealAddress_withBoundaryPrivateIps() throws Exception {
		// 测试边界内网IP地址
		String[] privateIps = { "10.0.0.0", "10.255.255.255", "172.16.0.0", "172.31.255.255", "192.168.0.0",
				"192.168.255.255" };
		for (String ip : privateIps) {
			String result = AddressUtils.getRealAddress(ip);
			Assertions.assertThat(result).isEqualTo("内网IP");
		}
	}

	@Test
	void test_getRealAddress_withNonPrivate172() throws Exception {
		// 测试172.15.x.x和172.32.x.x（非内网IP）
		String result1 = AddressUtils.getRealAddress("172.15.255.255");
		Assertions.assertThat(result1).isNotNull().isNotEqualTo("内网IP");
		String result2 = AddressUtils.getRealAddress("172.32.0.0");
		Assertions.assertThat(result2).isNotNull().isNotEqualTo("内网IP");
	}

}
