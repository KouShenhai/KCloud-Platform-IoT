/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.common.extension.register;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.laokou.common.extension.Extension;
import org.laokou.common.extension.ExtensionPointI;
import org.laokou.common.extension.Extensions;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * ExtensionBootstrap.
 *
 * @author Frank Zhang
 * @date 2020-06-18 7:55 PM
 */
@Component
@RequiredArgsConstructor
public class ExtensionBootstrap implements ApplicationContextAware {

	private final ExtensionRegister extensionRegister;

	private ApplicationContext applicationContext;

	@PostConstruct
	public void init() {
		Map<String, Object> extensionBeans = applicationContext.getBeansWithAnnotation(Extension.class);
		extensionBeans.values().forEach(extension -> extensionRegister.doRegistration((ExtensionPointI) extension));
		// handle @Extensions annotation
		Map<String, Object> extensionsBeans = applicationContext.getBeansWithAnnotation(Extensions.class);
		extensionsBeans.values()
			.forEach(extension -> extensionRegister.doRegistrationExtensions((ExtensionPointI) extension));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
