/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.sms.config;

import org.laokou.common.sms.service.SmsService;
import org.laokou.common.sms.service.impl.GYYSmsServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @author laokou
 */
@AutoConfiguration
public class SmsAutoConfig {

	@Bean("smsService")
	@ConditionalOnProperty(prefix = "sms", matchIfMissing = true, name = "type", havingValue = "GYY")
	public SmsService gyySmsServiceImpl(SmsProperties smsProperties) {
		return new GYYSmsServiceImpl(smsProperties);
	}

}
