/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.oss.model;

import lombok.Getter;
import lombok.Setter;
import org.laokou.admin.oss.model.enums.OperateType;
import org.laokou.common.i18n.annotation.Entity;

/**
 * OSS领域对象【实体】.
 *
 * @author laokou
 */
@Entity
@Getter
@Setter
public class OssE {

	private Long id;

	/**
	 * OSS的名称.
	 */
	@Setter
	@Getter
	private String name;

	/**
	 * OSS的类型.
	 */
	@Setter
	@Getter
	private String type;

	/**
	 * OSS的参数.
	 */
	@Setter
	@Getter
	private String param;

	/**
	 * OSS的状态 0启用 1禁用.
	 */
	@Setter
	@Getter
	private Integer status;

	/**
	 * OSS操作类型.
	 */
	@Setter
	@Getter
	private OperateType operateType;

	public OssE() {
		super();
	}

	//
	// private final IdGenerator idGenerator;
	//
	// public OssE(IdGenerator idGenerator) {
	// this.idGenerator = idGenerator;
	// }

	// public Long getPrimaryKey() {
	// return idGenerator.getId();
	// }
	//
	// public void checkOssParam() {
	// switch (ossOperateTypeEnum) {
	// case SAVE -> {
	// }
	// case MODIFY -> {
	// }
	// default -> {
	// }
	// }
	// }

}
