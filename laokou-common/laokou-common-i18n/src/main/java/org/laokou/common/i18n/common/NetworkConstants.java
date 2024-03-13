/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.common.i18n.common;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author laokou
 */
@Schema(name = "NetworkConstants", description = "网络常量")
public final class NetworkConstants {

	private NetworkConstants() {
	}

	@Schema(name = "IPV4_REGEX", description = "IPV4正则表达式")
	public static final String IPV4_REGEX = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";

	@Schema(name = "IP", description = "IP参数")
	public static final String IP = "ip";

	@Schema(name = "WWW", description = "www三级域名")
	public static final String WWW = "www";

	@Schema(name = "LOCAL_IPV4", description = "本地IP-IPV4")
	public static final String LOCAL_IPV4 = "127.0.0.1";

	@Schema(name = "LOCAL_IPV6", description = "本地IP-IPV6")
	public static final String LOCAL_IPV6 = "0:0:0:0:0:0:0:1";

	@Schema(name = "UNKNOWN_IP", description = "未知IP")
	public static final String UNKNOWN_IP = "unknown";

	@Schema(name = "LOCAL_DESC", description = "本地IP描述")
	public static final String LOCAL_DESC = "内网";

	@Schema(name = "HTTP_SCHEME", description = "http协议头")
	public static final String HTTP_SCHEME = "http://";

	@Schema(name = "HTTP_PROTOCOL", description = "http协议")
	public static final String HTTP_PROTOCOL = "http";

	@Schema(name = "HTTPS_PROTOCOL", description = "https协议")
	public static final String HTTPS_PROTOCOL = "https";

	@Schema(name = "HTTPS_SCHEME", description = "HTTPS协议头")
	public static final String HTTPS_SCHEME = "https://";

}
