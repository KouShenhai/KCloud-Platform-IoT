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

package org.laokou.oss.model;

import lombok.Setter;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.dto.AggregateRoot;

/**
 * @author laokou
 */
@Entity
public class OssA extends AggregateRoot {

	@Setter
	private FileFormatEnum fileFormatEnum;

	@Setter
	private String extName;

	@Setter
	private long size;

	public void checkSize() {
		if (size > 1024 * 1024 * 100) {
			throw new BizException("B_Oss_SizeExceeding100M", "文件大小不能超过100M");
		}
	}

	public void checkExt() {
		if (!fileFormatEnum.getExtNames().contains(extName)) {
			throw new BizException("B_Oss_ExtError", "文件格式错误");
		}
	}

}
