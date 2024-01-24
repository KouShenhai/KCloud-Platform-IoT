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

import java.util.regex.Pattern;

/**
 * @author laokou
 */
@Schema(name = "SysConstants", description = "系统变量")
public final class SysConstants {

	private SysConstants() {
	}

	@Schema(name = "VERSION", description = "版本")
	public static final String VERSION = "3.2.2";

	@Schema(name = "GRACEFUL_SHUTDOWN_URL", description = "优雅停机URL")
	public static final String GRACEFUL_SHUTDOWN_URL = "/graceful-shutdown";

	@Schema(name = "UNDEFINED", description = "UNDEFINED")
	public static final String UNDEFINED = "undefined";

	@Schema(name = "MAX_FILE_SIZE", description = "最大上传文件大小")
	public static final long MAX_FILE_SIZE = 100 * 1024 * 1024;

	@Schema(name = "ALGORITHM_RSA", description = "RSA加密算法")
	public static final String ALGORITHM_RSA = "RSA";

	@Schema(name = "ENABLED", description = "开启")
	public static final String ENABLED = "enabled";

	@Schema(name = "SPRING", description = "Spring")
	public static final String SPRING = "spring";

	@Schema(name = "EMPTY_LOG_MSG", description = "显示空日志信息")
	public static final String EMPTY_LOG_MSG = "暂无信息";

	@Schema(name = "ALGORITHM_AES", description = "AES加密加密算法")
	public static final String ALGORITHM_AES = "aes";

	@Schema(name = "AES_INSTANCE", description = "AES128对称加密算法")
	public static final String AES_INSTANCE = "AES/CBC/PKCS5Padding";

	@Schema(name = "COMMON_DATA_ID", description = "Nacos公共配置标识")
	public static final String COMMON_DATA_ID = "application-common.yaml";

	@Schema(name = "APPLICATION", description = "应用")
	public static final String APPLICATION = "application";

	@Schema(name = "RATE_LIMITER_KEY", description = "限流Key")
	public static final String RATE_LIMITER_KEY = "___%s_KEY___";

	@Schema(name = "REDIS_PROTOCOL_PREFIX", description = "Redis未加密连接")
	public static final String REDIS_PROTOCOL_PREFIX = "redis://";

	@Schema(name = "REDISS_PROTOCOL_PREFIX", description = "Redis加密连接")
	public static final String REDISS_PROTOCOL_PREFIX = "rediss://";

	@Schema(name = "CRYPTO_PREFIX", description = "加密前缀")
	public static final String CRYPTO_PREFIX = "ENC(";

	@Schema(name = "CRYPTO_SUFFIX", description = "加密后缀")
	public static final String CRYPTO_SUFFIX = ")";

	@Schema(name = "PUBLIC_KEY", description = "公钥Key")
	public static final String PUBLIC_KEY = "public-key";

	@Schema(name = "ALL_PATTERNS", description = "拦截所有路径")
	public static final String ALL_PATTERNS = "/**";

	@Schema(name = "EXCEL_EXT", description = "Excel文件后缀")
	public static final String EXCEL_EXT = ".xlsx";

	@Schema(name = "TLS_PROTOCOL_VERSION", description = "TLS协议版本")
	public static final String TLS_PROTOCOL_VERSION = "TLSv1.3";

	@Schema(name = "LINE_PATTERN", description = "下划线正则表达式")
	public static final Pattern LINE_PATTERN = Pattern.compile("_(\\w)");

	@Schema(name = "EMPTY_JSON", description = "空JSON字符串")
	public static final String EMPTY_JSON = "{}";

	@Schema(name = "DEFAULT_USERNAME", description = "默认账号")
	public static final String DEFAULT_USERNAME = "laokou";

	@Schema(name = "DEFAULT_PASSWORD", description = "默认密码")
	public static final String DEFAULT_PASSWORD = "laokou123";

	@Schema(name = "DOWN_STATUS", description = "未通过健康检查")
	public static final String DOWN_STATUS = "DOWN";

	@Schema(name = "OFFLINE_STATUS", description = "离线")
	public static final String OFFLINE_STATUS = "OFFLINE";

	@Schema(name = "UP_STATUS", description = "上线")
	public static final String UP_STATUS = "UP";

	@Schema(name = "UNKNOWN_STATUS", description = "未知异常")
	public static final String UNKNOWN_STATUS = "UNKNOWN";

	@Schema(name = "DEFAULT_MESSAGE", description = "默认消息")
	public static final String DEFAULT_MESSAGE = "您有一条未读消息，请注意查收";

	@Schema(name = "WEBSOCKET_PATH", description = "WebSocket路径")
	public static final String WEBSOCKET_PATH = "/ws";

	@Schema(name = "THREAD_POOL_TASK_EXECUTOR_NAME", description = "线程池名称")
	public static final String THREAD_POOL_TASK_EXECUTOR_NAME = "executor";

}
