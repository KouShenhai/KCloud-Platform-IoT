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

package org.laokou.common.i18n.utils;
import static org.laokou.common.i18n.common.StatusCodes.INTERNAL_SERVER_ERROR;
import static org.laokou.common.i18n.common.SysConstants.EMPTY_LOG_MSG;

/**
 * @author laokou
 */
public class LogUtil {

	public static String result(String msg) {
		return StringUtil.isEmpty(msg) ? EMPTY_LOG_MSG : msg;
	}

	public static String fail(String msg) {
		return StringUtil.isEmpty(msg) ? MessageUtil.getMessage(INTERNAL_SERVER_ERROR) : msg;
	}

}
