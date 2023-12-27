/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

/**
 * @author laokou
 */
public interface Constant {

	/**
	 * 分割参数
	 */
	String SLASH = "/";

	/**
	 * 分割参数
	 */
	String ERECT = "|";

	/**
	 * 分割参数
	 */
	String DROP = "、";

	/**
	 * 分割参数
	 */
	String BACKSLASH = "\\";

	/**
	 * 分割参数
	 */
	String STAR = "*";

	/**
	 * 分割参数
	 */
	String AT = "@";

	/**
	 * 百分号
	 */
	String PERCENT = "%";

	/**
	 * 分割参数
	 */
	String DOT = ".";

	/**
	 * 分割参数
	 */
	String RISK = ":";

	/**
	 * 分割参数
	 */
	String COMMA = ",";

	/**
	 * 分割参数
	 */
	String CHINESE_COMMA = "，";

	/**
	 * 左括号
	 */
	String LEFT = "(";

	/**
	 * 右括号
	 */
	String RIGHT = ")";

	/**
	 * 空格
	 */
	String SPACE = " ";

	/**
	 * 等于
	 */
	String EQUAL = "=";

	/**
	 * 模糊
	 */
	String LIKE = "like";

	/**
	 * 分割参数
	 */
	String UNDER = "_";

	/**
	 * 分割参数
	 */
	String BAR = "-";

	/**
	 * 双引号
	 */
	String DOUBLE_QUOT = "\"";

	/**
	 * 单引号
	 */
	String SINGLE_QUOT = "'";

	/**
	 * 空
	 */
	String EMPTY = "";

	/**
	 * 并
	 */
	String AND = "&";

	/**
	 * 或
	 */
	String OR = "or";

	/**
	 * 分割参数
	 */
	String MARK = "?";

	/**
	 * 分割参数
	 */
	String ROD = "-";

	/**
	 * 分割参数
	 */
	String DOLLAR = "$";

	/**
	 * true
	 */
	String TRUE = "true";

	/**
	 * false
	 */
	String FALSE = "false";

	/**
	 * 默认
	 */
	int DEFAULT = 0;

	/**
	 * 成功
	 */
	int SUCCESS = 0;

	/**
	 * 失败
	 */
	int FAIL = 1;

	/**
	 * 默认租户
	 */
	long DEFAULT_TENANT = 0;

	/**
	 * RSA
	 */
	String ALGORITHM_RSA = "RSA";

	/**
	 * 本地网络描述
	 */
	String LOCAL_NETWORK_LABEL = "内网";

	/**
	 * 本地IP
	 */
	String LOCAL_IP = "127.0.0.1";

	/**
	 * IP未知
	 */
	String IP_UNKNOWN = "unknown";

	/**
	 * 本地网段
	 */
	String LOCAL_NETWORK_SEGMENT = "0:0:0:0:0:0:0:1";

	/**
	 * spring管理
	 */
	String SPRING = "spring";

	/**
	 * 开启
	 */
	String ENABLED = "enabled";

	/**
	 * 认证
	 */
	String AUTHORIZATION = "Authorization";

	/**
	 * 用户名
	 */
	String USERNAME = "username";

	/**
	 * 密码
	 */
	String PASSWORD = "password";

	/**
	 * 用户名
	 */
	String USER_NAME = "user-name";

	/**
	 * 链路ID
	 */
	String TRACE_ID = "trace-id";

	/**
	 * 用户ID
	 */
	String USER_ID = "user-id";

	/**
	 * 租户ID
	 */
	String TENANT_ID = "tenant-id";

	/**
	 * 服务IP
	 */
	String SERVICE_HOST = "service-host";

	/**
	 * 服务端口
	 */
	String SERVICE_PORT = "service-port";

	/**
	 * 优雅停机
	 */
	String GRACEFUL_SHUTDOWN_URL = "/graceful-shutdown";

	/**
	 * 域名
	 */
	String DOMAIN_NAME = "domain-name";

	/**
	 * 邮箱
	 */
	String MAIL = "mail";

	/**
	 * 手机
	 */
	String MOBILE = "mobile";

	/**
	 * 版本
	 */
	String VERSION = "3.2.0";

	/**
	 * IP参数
	 */
	String IP = "ip";

	/**
	 * https协议
	 */
	String HTTPS_SCHEME = "https";

	/**
	 * www域名前缀
	 */
	String WWW = "www";

	/**
	 * 应用名称YAML配置
	 */
	String SPRING_APPLICATION_NAME = "spring.application.name";

	/**
	 * IPV4正则表达式
	 */
	String IPV4_REGEX = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";

	/**
	 * 资源索引
	 */
	String RESOURCE_INDEX = "laokou_resource";

	/**
	 * 分布式链路索引
	 */
	String TRACE_INDEX = "laokou_trace";

}
