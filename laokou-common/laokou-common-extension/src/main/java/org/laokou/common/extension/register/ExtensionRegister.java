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

/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */

package org.laokou.common.extension.register;

import lombok.RequiredArgsConstructor;
import org.laokou.common.extension.*;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

/**
 * ExtensionRegister.
 *
 * @author fulan.zjf 2017-11-05
 */
@Component
@RequiredArgsConstructor
public class ExtensionRegister {

	public final static String EXTENSION_EXT_PT_NAMING = "ExtPt";

	/**
	 * 扩展点接口名称不合法.
	 */
	private static final String EXTENSION_INTERFACE_NAME_ILLEGAL = "extension_interface_name_illegal";

	/**
	 * 扩展点不合法.
	 */
	private static final String EXTENSION_ILLEGAL = "extension_illegal";

	/**
	 * 扩展点定义重复.
	 */
	private static final String EXTENSION_DEFINE_DUPLICATE = "extension_define_duplicate";

	private final ExtensionRepository extensionRepository;

	public void doRegistration(ExtensionPointI extensionObject) {
		Class<?> extensionClz = extensionObject.getClass();
		if (AopUtils.isAopProxy(extensionObject)) {
			extensionClz = AopUtils.getTargetClass(extensionObject);
		}
		Extension extensionAnn = AnnotatedElementUtils.findMergedAnnotation(extensionClz, Extension.class);
		assert extensionAnn != null;
		BizScenario bizScenario = BizScenario.valueOf(extensionAnn.bizId(), extensionAnn.useCase(),
				extensionAnn.scenario());
		ExtensionCoordinate extensionCoordinate = new ExtensionCoordinate(calculateExtensionPoint(extensionClz),
				bizScenario.getUniqueIdentity());
		ExtensionPointI preVal = extensionRepository.getExtensionRepo().put(extensionCoordinate, extensionObject);
		if (preVal != null) {
			String errMessage = "Duplicate registration is not allowed for :" + extensionCoordinate;
			throw new ExtensionException(EXTENSION_DEFINE_DUPLICATE, errMessage);
		}
	}

	public void doRegistrationExtensions(ExtensionPointI extensionObject) {
		Class<?> extensionClz = extensionObject.getClass();
		if (AopUtils.isAopProxy(extensionObject)) {
			extensionClz = ClassUtils.getUserClass(extensionObject);
		}

		Extensions extensionsAnnotation = AnnotationUtils.findAnnotation(extensionClz, Extensions.class);
		assert extensionsAnnotation != null;
		Extension[] extensions = extensionsAnnotation.value();
		if (!ObjectUtils.isEmpty(extensions)) {
			for (Extension extensionAnn : extensions) {
				BizScenario bizScenario = BizScenario.valueOf(extensionAnn.bizId(), extensionAnn.useCase(),
						extensionAnn.scenario());
				ExtensionCoordinate extensionCoordinate = new ExtensionCoordinate(calculateExtensionPoint(extensionClz),
						bizScenario.getUniqueIdentity());
				ExtensionPointI preVal = extensionRepository.getExtensionRepo()
					.put(extensionCoordinate, extensionObject);
				if (preVal != null) {
					String errMessage = "Duplicate registration is not allowed for :" + extensionCoordinate;
					throw new ExtensionException(EXTENSION_DEFINE_DUPLICATE, errMessage);
				}
			}
		}

		//
		String[] bizIds = extensionsAnnotation.bizId();
		String[] useCases = extensionsAnnotation.useCase();
		String[] scenarios = extensionsAnnotation.scenario();
		for (String bizId : bizIds) {
			for (String useCase : useCases) {
				for (String scenario : scenarios) {
					BizScenario bizScenario = BizScenario.valueOf(bizId, useCase, scenario);
					ExtensionCoordinate extensionCoordinate = new ExtensionCoordinate(
							calculateExtensionPoint(extensionClz), bizScenario.getUniqueIdentity());
					ExtensionPointI preVal = extensionRepository.getExtensionRepo()
						.put(extensionCoordinate, extensionObject);
					if (preVal != null) {
						String errMessage = "Duplicate registration is not allowed for :" + extensionCoordinate;
						throw new ExtensionException(EXTENSION_DEFINE_DUPLICATE, errMessage);
					}
				}
			}
		}
	}

	private String calculateExtensionPoint(Class<?> targetClz) {
		Class<?>[] interfaces = ClassUtils.getAllInterfacesForClass(targetClz);
		if (interfaces.length == 0) {
			throw new ExtensionException(EXTENSION_ILLEGAL,
					"Please assign a extension point interface for " + targetClz);
		}
		for (Class<?> inter : interfaces) {
			String extensionPoint = inter.getSimpleName();
			if (extensionPoint.contains(EXTENSION_EXT_PT_NAMING)) {
				return inter.getName();
			}
		}
		String errMessage = "Your name of ExtensionPoint for " + targetClz + " is not valid, must be end of "
				+ EXTENSION_EXT_PT_NAMING;
		throw new ExtensionException(EXTENSION_INTERFACE_NAME_ILLEGAL, errMessage);
	}

}
