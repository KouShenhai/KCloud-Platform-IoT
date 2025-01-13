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

package org.laokou.common.extension;

import lombok.Getter;

/**
 * Extension Coordinate(扩展坐标) is used to uniquely position an Extension.
 *
 * @author fulan.zjf 2017-11-05
 */
public class ExtensionCoordinate {

	private final String extensionPointName;

	private final String bizScenarioUniqueIdentity;

	/**
	 * Wrapper.
	 */
	private Class<?> extensionPointClass;

	@Getter
	private BizScenario bizScenario;

	public ExtensionCoordinate(Class<?> extPtClass, BizScenario bizScenario) {
		this.extensionPointClass = extPtClass;
		this.extensionPointName = extPtClass.getName();
		this.bizScenario = bizScenario;
		this.bizScenarioUniqueIdentity = bizScenario.getUniqueIdentity();
	}

	public ExtensionCoordinate(String extensionPoint, String bizScenario) {
		this.extensionPointName = extensionPoint;
		this.bizScenarioUniqueIdentity = bizScenario;
	}

	public static ExtensionCoordinate valueOf(Class<?> extPtClass, BizScenario bizScenario) {
		return new ExtensionCoordinate(extPtClass, bizScenario);
	}

	public Class getExtensionPointClass() {
		return extensionPointClass;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bizScenarioUniqueIdentity == null) ? 0 : bizScenarioUniqueIdentity.hashCode());
		result = prime * result + ((extensionPointName == null) ? 0 : extensionPointName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		ExtensionCoordinate other = (ExtensionCoordinate) obj;
		if (bizScenarioUniqueIdentity == null) {
			if (other.bizScenarioUniqueIdentity != null) {
				return false;
			}
		}
		else if (!bizScenarioUniqueIdentity.equals(other.bizScenarioUniqueIdentity)) {
			return false;
		}
		if (extensionPointName == null) {
			return other.extensionPointName == null;
		}
		else {
			return extensionPointName.equals(other.extensionPointName);
		}
	}

	@Override
	public String toString() {
		return "ExtensionCoordinate [extensionPointName=" + extensionPointName + ", bizScenarioUniqueIdentity="
				+ bizScenarioUniqueIdentity + "]";
	}

}
