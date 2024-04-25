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

package org.laokou.auth.api;

import org.laokou.auth.dto.captcha.CaptchaGetQry;
import org.laokou.common.i18n.dto.Result;

/**
 * 验证码.
 *
 * @author laokou
 */
public interface CaptchasServiceI {

	/**
	 * 获取验证码.
	 * @param qry 获取验证码参数
	 * @return 验证码
	 */
	Result<String> getByUuid(CaptchaGetQry qry);

}
