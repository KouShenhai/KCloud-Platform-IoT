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

package org.laokou.common.core;

import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.IpUtils;
import org.laokou.common.i18n.common.exception.BizException;
import org.mockito.Mockito;

/**
 * @author laokou
 */
class IpUtilsTest {

	@Test
	void test_getIpAddr_withNullRequest() {
		// 测试null请求对象
		String result = IpUtils.getIpAddr(null);
		Assertions.assertThat(result).isEqualTo(IpUtils.UNKNOWN_IP);
	}

	@Test
	void test_getIpAddr_withXForwardedFor() {
		// 测试X-Forwarded-For header
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("192.168.1.100");
		String result = IpUtils.getIpAddr(request);
		Assertions.assertThat(result).isEqualTo("192.168.1.100");
	}

	@Test
	void test_getIpAddr_withProxyClientIP() {
		// 测试Proxy-Client-IP header
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn(null);
		Mockito.when(request.getHeader("Proxy-Client-IP")).thenReturn("10.0.0.1");
		String result = IpUtils.getIpAddr(request);
		Assertions.assertThat(result).isEqualTo("10.0.0.1");
	}

	@Test
	void test_getIpAddr_withXForwardedForUpperCase() {
		// 测试X-Forwarded-For header（大写）
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn(null);
		Mockito.when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("172.16.0.1");
		String result = IpUtils.getIpAddr(request);
		Assertions.assertThat(result).isEqualTo("172.16.0.1");
	}

	@Test
	void test_getIpAddr_withWLProxyClientIP() {
		// 测试WL-Proxy-Client-IP header
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn(null);
		Mockito.when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn(null);
		Mockito.when(request.getHeader("WL-Proxy-Client-IP")).thenReturn("203.0.113.1");
		String result = IpUtils.getIpAddr(request);
		Assertions.assertThat(result).isEqualTo("203.0.113.1");
	}

	@Test
	void test_getIpAddr_withXRealIP() {
		// 测试X-Real-IP header
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn(null);
		Mockito.when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn(null);
		Mockito.when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
		Mockito.when(request.getHeader("X-Real-IP")).thenReturn("8.8.8.8");
		String result = IpUtils.getIpAddr(request);
		Assertions.assertThat(result).isEqualTo("8.8.8.8");
	}

	@Test
	void test_getIpAddr_withRemoteAddr() {
		// 测试getRemoteAddr方法
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn(null);
		Mockito.when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn(null);
		Mockito.when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
		Mockito.when(request.getHeader("X-Real-IP")).thenReturn(null);
		Mockito.when(request.getRemoteAddr()).thenReturn("114.114.114.114");
		String result = IpUtils.getIpAddr(request);
		Assertions.assertThat(result).isEqualTo("114.114.114.114");
	}

	@Test
	void test_getIpAddr_withIPv6Localhost() {
		// 测试IPv6本地地址转换为IPv4
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn(null);
		Mockito.when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn(null);
		Mockito.when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
		Mockito.when(request.getHeader("X-Real-IP")).thenReturn(null);
		Mockito.when(request.getRemoteAddr()).thenReturn("0:0:0:0:0:0:0:1");
		String result = IpUtils.getIpAddr(request);
		Assertions.assertThat(result).isEqualTo(IpUtils.LOCAL_IPV4);
	}

	@Test
	void test_getIpAddr_withEmptyRemoteAddr() {
		// 测试空字符串remoteAddr转换为本地IP
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn(null);
		Mockito.when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn(null);
		Mockito.when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
		Mockito.when(request.getHeader("X-Real-IP")).thenReturn(null);
		Mockito.when(request.getRemoteAddr()).thenReturn("");
		String result = IpUtils.getIpAddr(request);
		Assertions.assertThat(result).isEqualTo(IpUtils.LOCAL_IPV4);
	}

	@Test
	void test_getIpAddr_withMultipleIps() {
		// 测试多个IP地址（逗号分隔），取第一个
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("192.168.1.1, 10.0.0.1, 172.16.0.1");
		String result = IpUtils.getIpAddr(request);
		Assertions.assertThat(result).isEqualTo("192.168.1.1");
	}

	@Test
	void test_getIpAddr_withUnknownHeader() {
		// 测试unknown header值
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("unknown");
		Mockito.when(request.getHeader("Proxy-Client-IP")).thenReturn("192.168.1.1");
		String result = IpUtils.getIpAddr(request);
		Assertions.assertThat(result).isEqualTo("192.168.1.1");
	}

	@Test
	void test_getIpAddr_withUnknownCaseInsensitive() {
		// 测试UNKNOWN（大小写不敏感）
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("UNKNOWN");
		Mockito.when(request.getHeader("Proxy-Client-IP")).thenReturn("10.0.0.1");
		String result = IpUtils.getIpAddr(request);
		Assertions.assertThat(result).isEqualTo("10.0.0.1");
	}

	// ==================== internalIp 方法测试 ====================

	@Test
	void test_internalIp_withIPv6Localhost() {
		// 测试IPv6本地地址
		Assertions.assertThat(IpUtils.internalIp("0:0:0:0:0:0:0:1")).isTrue();
	}

	@Test
	void test_internalIp_withLocalhost() {
		// 测试127.0.0.1
		Assertions.assertThat(IpUtils.internalIp("127.0.0.1")).isTrue();
	}

	@Test
	void test_internalIp_with10Network() {
		// 测试10.x.x.x网段
		Assertions.assertThat(IpUtils.internalIp("10.0.0.0")).isTrue();
		Assertions.assertThat(IpUtils.internalIp("10.0.0.1")).isTrue();
		Assertions.assertThat(IpUtils.internalIp("10.255.255.255")).isTrue();
		Assertions.assertThat(IpUtils.internalIp("10.128.64.32")).isTrue();
	}

	@Test
	void test_internalIp_with172Network() {
		// 测试172.16.x.x - 172.31.x.x网段
		Assertions.assertThat(IpUtils.internalIp("172.16.0.0")).isTrue();
		Assertions.assertThat(IpUtils.internalIp("172.16.0.1")).isTrue();
		Assertions.assertThat(IpUtils.internalIp("172.31.255.255")).isTrue();
		Assertions.assertThat(IpUtils.internalIp("172.20.10.5")).isTrue();
		// 测试边界值（非内网）
		Assertions.assertThat(IpUtils.internalIp("172.15.255.255")).isFalse();
		Assertions.assertThat(IpUtils.internalIp("172.32.0.0")).isFalse();
	}

	@Test
	void test_internalIp_with192Network() {
		// 测试192.168.x.x网段
		Assertions.assertThat(IpUtils.internalIp("192.168.0.0")).isTrue();
		Assertions.assertThat(IpUtils.internalIp("192.168.0.1")).isTrue();
		Assertions.assertThat(IpUtils.internalIp("192.168.255.255")).isTrue();
		Assertions.assertThat(IpUtils.internalIp("192.168.1.100")).isTrue();
	}

	@Test
	void test_internalIp_withPublicIp() {
		// 测试公网IP
		Assertions.assertThat(IpUtils.internalIp("8.8.8.8")).isFalse();
		Assertions.assertThat(IpUtils.internalIp("114.114.114.114")).isFalse();
		Assertions.assertThat(IpUtils.internalIp("223.5.5.5")).isFalse();
		Assertions.assertThat(IpUtils.internalIp("180.76.76.76")).isFalse();
		Assertions.assertThat(IpUtils.internalIp("1.1.1.1")).isFalse();
	}

	@Test
	void test_internalIp_withInvalidIp() {
		// 测试无效IP
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("256.256.256.256"))
			.isExactlyInstanceOf(BizException.class);
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("invalid.ip")).isExactlyInstanceOf(BizException.class);
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("192.168")).isExactlyInstanceOf(BizException.class);
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("192.168.1.x")).isExactlyInstanceOf(BizException.class);
	}

	@Test
	void test_internalIp_withNull() {
		// 测试null
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp(null)).isExactlyInstanceOf(BizException.class);
	}

	@Test
	void test_internalIp_withEmptyString() {
		// 测试空字符串
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("")).isExactlyInstanceOf(BizException.class);
	}

	@Test
	void test_internalIp_withBoundaryValues() {
		// 测试边界值
		// 10网段边界
		Assertions.assertThat(IpUtils.internalIp("10.0.0.0")).isTrue();
		Assertions.assertThat(IpUtils.internalIp("10.255.255.255")).isTrue();
		// 172网段边界
		Assertions.assertThat(IpUtils.internalIp("172.16.0.0")).isTrue();
		Assertions.assertThat(IpUtils.internalIp("172.31.255.255")).isTrue();
		// 192网段边界
		Assertions.assertThat(IpUtils.internalIp("192.168.0.0")).isTrue();
		Assertions.assertThat(IpUtils.internalIp("192.168.255.255")).isTrue();
	}

	@Test
	void test_internalIp_withNonPrivate172() {
		// 测试172网段非内网部分
		Assertions.assertThat(IpUtils.internalIp("172.15.255.255")).isFalse();
		Assertions.assertThat(IpUtils.internalIp("172.32.0.0")).isFalse();
		Assertions.assertThat(IpUtils.internalIp("172.0.0.0")).isFalse();
		Assertions.assertThat(IpUtils.internalIp("172.255.255.255")).isFalse();
	}

	@Test
	void test_internalIp_withNonPrivate192() {
		// 测试192网段非内网部分
		Assertions.assertThat(IpUtils.internalIp("192.167.255.255")).isFalse();
		Assertions.assertThat(IpUtils.internalIp("192.169.0.0")).isFalse();
		Assertions.assertThat(IpUtils.internalIp("192.0.0.0")).isFalse();
		Assertions.assertThat(IpUtils.internalIp("192.255.255.255")).isFalse();
	}

	@Test
	void test_internalIp_withSpecialCases() {
		// 测试特殊情况
		Assertions.assertThat(IpUtils.internalIp("0.0.0.0")).isFalse();
		Assertions.assertThat(IpUtils.internalIp("255.255.255.255")).isFalse();
		Assertions.assertThat(IpUtils.internalIp("169.254.1.1")).isFalse(); // 链路本地地址
	}

	// ==================== textToNumericFormatV4 间接测试（通过 internalIp） ====================

	@Test
	void test_internalIp_withSingleSegment() {
		// 测试单段IP格式（通过internalIp间接测试textToNumericFormatV4的case 1）
		// 单段IP会被转换为长整型，需要测试边界值
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("0")).isExactlyInstanceOf(BizException.class);
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("4294967295")).isExactlyInstanceOf(BizException.class); // 最大值
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("4294967296")).isExactlyInstanceOf(BizException.class); // 超出范围
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("-1")).isExactlyInstanceOf(BizException.class); // 负数
	}

	@Test
	void test_internalIp_withTwoSegments() {
		// 测试两段IP格式（通过internalIp间接测试textToNumericFormatV4的case 2）
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("192.168")).isExactlyInstanceOf(BizException.class);
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("10.0")).isExactlyInstanceOf(BizException.class);
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("172.16")).isExactlyInstanceOf(BizException.class);
		// 测试边界值
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("255.16777215")).isExactlyInstanceOf(BizException.class); // 最大值
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("256.0")).isExactlyInstanceOf(BizException.class); // 超出范围
	}

	@Test
	void test_internalIp_withThreeSegments() {
		// 测试三段IP格式（通过internalIp间接测试textToNumericFormatV4的case 3）
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("192.168.1")).isExactlyInstanceOf(BizException.class);
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("10.0.0")).isExactlyInstanceOf(BizException.class);
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("172.16.0")).isExactlyInstanceOf(BizException.class);
		// 测试边界值
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("255.255.65535"))
			.isExactlyInstanceOf(BizException.class); // 最大值
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("256.0.0")).isExactlyInstanceOf(BizException.class); // 超出范围
	}

	@Test
	void test_internalIp_withMoreThanFourSegments() {
		// 测试超过四段的IP（通过internalIp间接测试textToNumericFormatV4的default case）
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("192.168.1.1.1"))
			.isExactlyInstanceOf(BizException.class);
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("10.0.0.0.0")).isExactlyInstanceOf(BizException.class);
	}

	@Test
	void test_internalIp_withNonNumericSegments() {
		// 测试非数字段（通过internalIp间接测试textToNumericFormatV4的NumberFormatException）
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("192.168.1.x")).isExactlyInstanceOf(BizException.class);
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("abc.def.ghi.jkl"))
			.isExactlyInstanceOf(BizException.class);
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("192.a.1.1")).isExactlyInstanceOf(BizException.class);
	}

	@Test
	void test_internalIp_withOutOfRangeSegments() {
		// 测试超出范围的段值
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("256.0.0.0")).isExactlyInstanceOf(BizException.class);
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("0.256.0.0")).isExactlyInstanceOf(BizException.class);
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("0.0.256.0")).isExactlyInstanceOf(BizException.class);
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("0.0.0.256")).isExactlyInstanceOf(BizException.class);
		Assertions.assertThatThrownBy(() -> IpUtils.internalIp("-1.0.0.0")).isExactlyInstanceOf(BizException.class);
	}

	@Test
	void test_getIpAddr_withAllHeadersEmpty() {
		// 测试所有header都为空，最终使用getRemoteAddr
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn(null);
		Mockito.when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn(null);
		Mockito.when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
		Mockito.when(request.getHeader("X-Real-IP")).thenReturn(null);
		Mockito.when(request.getRemoteAddr()).thenReturn("192.168.1.1");
		String result = IpUtils.getIpAddr(request);
		Assertions.assertThat(result).isEqualTo("192.168.1.1");
	}

	@Test
	void test_getIpAddr_withAllHeadersUnknown() {
		// 测试所有header都是unknown，最终使用getRemoteAddr
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("unknown");
		Mockito.when(request.getHeader("Proxy-Client-IP")).thenReturn("unknown");
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("unknown");
		Mockito.when(request.getHeader("WL-Proxy-Client-IP")).thenReturn("unknown");
		Mockito.when(request.getHeader("X-Real-IP")).thenReturn("unknown");
		Mockito.when(request.getRemoteAddr()).thenReturn("10.0.0.1");
		String result = IpUtils.getIpAddr(request);
		Assertions.assertThat(result).isEqualTo("10.0.0.1");
	}

	@Test
	void test_getIpAddr_withAllHeadersEmptyString() {
		// 测试所有header都是空字符串，最终使用getRemoteAddr
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("");
		Mockito.when(request.getHeader("Proxy-Client-IP")).thenReturn("");
		Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("");
		Mockito.when(request.getHeader("WL-Proxy-Client-IP")).thenReturn("");
		Mockito.when(request.getHeader("X-Real-IP")).thenReturn("");
		Mockito.when(request.getRemoteAddr()).thenReturn("172.16.0.1");
		String result = IpUtils.getIpAddr(request);
		Assertions.assertThat(result).isEqualTo("172.16.0.1");
	}

	@Test
	void test_internalIp_withSamplePrivateNetworkRanges() {
		// 测试内网IP范围的样本值
		// 10.0.0.0/8 样本
		Assertions.assertThat(IpUtils.internalIp("10.0.0.1")).isTrue();
		Assertions.assertThat(IpUtils.internalIp("10.128.64.32")).isTrue();
		Assertions.assertThat(IpUtils.internalIp("10.255.255.254")).isTrue();
		// 172.16.0.0/12 样本
		Assertions.assertThat(IpUtils.internalIp("172.16.0.1")).isTrue();
		Assertions.assertThat(IpUtils.internalIp("172.20.10.5")).isTrue();
		Assertions.assertThat(IpUtils.internalIp("172.31.255.254")).isTrue();
		// 192.168.0.0/16 样本
		Assertions.assertThat(IpUtils.internalIp("192.168.0.1")).isTrue();
		Assertions.assertThat(IpUtils.internalIp("192.168.128.64")).isTrue();
		Assertions.assertThat(IpUtils.internalIp("192.168.255.254")).isTrue();
	}

}
