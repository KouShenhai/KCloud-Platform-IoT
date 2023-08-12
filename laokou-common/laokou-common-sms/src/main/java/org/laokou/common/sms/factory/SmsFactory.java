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

package org.laokou.common.sms.factory;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.common.CustomException;
import org.laokou.common.sms.enums.SmsTypeEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class SmsFactory {

	@Value("${sms.type}")
	private Integer type;

	private final GuoYangYunSmsServiceImpl guoYangYunServiceImpl;

	public SmsService build() {
		if (SmsTypeEnum.GUO_YANG_YUN.ordinal() == type) {
			return guoYangYunServiceImpl;
		}
		throw new CustomException("请检查SMS配置");
	}

}
