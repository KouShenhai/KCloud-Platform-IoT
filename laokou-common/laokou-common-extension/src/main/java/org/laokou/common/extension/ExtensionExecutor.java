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

/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */

package org.laokou.common.extension;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.extension.register.AbstractComponentExecutor;
import org.springframework.stereotype.Component;

/**
 * ExtensionExecutor.
 *
 * @author fulan.zjf 2017-11-05
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExtensionExecutor extends AbstractComponentExecutor {

	private static final String EXTENSION_NOT_FOUND = "extension_not_found";

	private final ExtensionRepository extensionRepository;

	@Override
	protected <C> C locateComponent(Class<C> targetClz, BizScenario bizScenario) {
		C extension = locateExtension(targetClz, bizScenario);
		log.debug("[Located Extension]: {}", extension.getClass().getSimpleName());
		return extension;
	}

	/**
	 * if the bizScenarioUniqueIdentity is "ali.tmall.supermarket"
	 * <p>
	 * the search path is as below: 1、first try to get extension by
	 * "ali.tmall.supermarket", if get, return it. 2、loop try to get extension by
	 * "ali.tmall", if get, return it. 3、loop try to get extension by "ali", if get,
	 * return it. 4、if not found, try the default extension.
	 * @param <Ext> ext
	 * @param bizScenario 业务码
	 * @param targetClz 类
	 */
	protected <Ext> Ext locateExtension(Class<Ext> targetClz, BizScenario bizScenario) {
		checkNull(bizScenario);

		Ext extension;

		log.debug("BizScenario in locateExtension is : " + bizScenario.getUniqueIdentity());

		// first try with full namespace
		extension = firstTry(targetClz, bizScenario);
		if (extension != null) {
			return extension;
		}

		// second try with default scenario
		extension = secondTry(targetClz, bizScenario);
		if (extension != null) {
			return extension;
		}

		// third try with default use case + default scenario
		extension = defaultUseCaseTry(targetClz, bizScenario);
		if (extension != null) {
			return extension;
		}

		String errMessage = "Can not find extension with ExtensionPoint: " + targetClz + " BizScenario:"
				+ bizScenario.getUniqueIdentity();
		throw new ExtensionException(EXTENSION_NOT_FOUND, errMessage);
	}

	/**
	 * first try with full namespace.
	 * <p>
	 * example: biz1.useCase1.scenario1
	 * @param <Ext> ext
	 * @param bizScenario 业务码
	 * @param targetClz 类
	 */
	private <Ext> Ext firstTry(Class<Ext> targetClz, BizScenario bizScenario) {
		log.debug("First trying with {}", bizScenario.getUniqueIdentity());
		return locate(targetClz.getName(), bizScenario.getUniqueIdentity());
	}

	/**
	 * second try with default scenario.
	 * <p>
	 * example: biz1.useCase1.#defaultScenario#
	 * @param <Ext> ext.
	 * @param bizScenario 业务码.
	 * @param targetClz 类.
	 */
	private <Ext> Ext secondTry(Class<Ext> targetClz, BizScenario bizScenario) {
		log.debug("Second trying with {}", bizScenario.getIdentityWithDefaultScenario());
		return locate(targetClz.getName(), bizScenario.getIdentityWithDefaultScenario());
	}

	/**
	 * third try with default use case + default scenario.
	 * <p>
	 * example: biz1.#defaultUseCase#.#defaultScenario#
	 * @param <Ext> ext
	 * @param bizScenario 业务码
	 * @param targetClz 类
	 */
	private <Ext> Ext defaultUseCaseTry(Class<Ext> targetClz, BizScenario bizScenario) {
		log.debug("Third trying with {}", bizScenario.getIdentityWithDefaultUseCase());
		return locate(targetClz.getName(), bizScenario.getIdentityWithDefaultUseCase());
	}

	private <Ext> Ext locate(String name, String uniqueIdentity) {
		return (Ext) extensionRepository.getExtensionRepo().get(new ExtensionCoordinate(name, uniqueIdentity));
	}

	private void checkNull(BizScenario bizScenario) {
		if (bizScenario == null) {
			throw new IllegalArgumentException("BizScenario can not be null for extension");
		}
	}

}
