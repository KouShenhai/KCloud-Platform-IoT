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

package org.laokou.common.extension;

import org.laokou.common.extension.register.ExtensionBootstrap;
import org.laokou.common.extension.register.ExtensionRegister;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ Description :
 * @ Author : Frank Zhang
 * @ CreateDate : 2020/11/09
 * @ Version : 1.0
 */
@Configuration
public class ExtensionAutoConfiguration {

	@Bean(initMethod = "init")
	@ConditionalOnMissingBean(ExtensionBootstrap.class)
	public ExtensionBootstrap bootstrap(ExtensionRegister extensionRegister) {
		return new ExtensionBootstrap(extensionRegister);
	}

	@Bean
	@ConditionalOnMissingBean(ExtensionRepository.class)
	public ExtensionRepository repository() {
		return new ExtensionRepository();
	}

	@Bean
	@ConditionalOnMissingBean(ExtensionExecutor.class)
	public ExtensionExecutor executor(ExtensionRepository extensionRepository) {
		return new ExtensionExecutor(extensionRepository);
	}

	@Bean
	@ConditionalOnMissingBean(ExtensionRegister.class)
	public ExtensionRegister register(ExtensionRepository extensionRepository) {
		return new ExtensionRegister(extensionRepository);
	}

}
