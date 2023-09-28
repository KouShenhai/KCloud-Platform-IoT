/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.admin.common;

/**
 * @author laokou
 */
public interface Constant {

	/**
	 * 租户
	 */
	String TENANT = "#tenant";

	/**
	 * 用户
	 */
	String USER = "user";

	/**
	 * 登录日志
	 */
	String LOGIN_LOG = "login_log";

	/**
	 * 默认数据源
	 */
	long DEFAULT_TENANT = 0;

	/**
	 * 最大文件大小
	 */
	long MAX_FILE_SIZE = 100 * 1024 * 1024;

}
