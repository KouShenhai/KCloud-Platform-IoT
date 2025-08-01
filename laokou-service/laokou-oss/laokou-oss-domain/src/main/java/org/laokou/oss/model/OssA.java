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

import lombok.Getter;
import lombok.Setter;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.dto.IdGenerator;
import org.laokou.common.i18n.util.ObjectUtils;
import java.util.function.Supplier;

/**
 * @author laokou
 */
@Entity
public class OssA extends AggregateRoot {

	@Setter
	@Getter
	private FileFormatEnum fileFormatEnum;

	@Setter
	@Getter
	private String extName;

	@Setter
	@Getter
	private long size;

	@Setter
	@Getter
	private byte[] buffer;

	@Setter
	@Getter
	private String name;

	@Setter
	@Getter
	private String contentType;

	@Getter
	private String url;

	@Setter
	@Getter
	private String md5;

	@Setter
	@Getter
	private String fileFormat;

	@Getter
	private boolean publishEvent = false;

	@Getter
	private Long ossId;

	private final IdGenerator idGenerator;

	public OssA(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public void checkSize() {
		if (size > 1024 * 1024 * 100) {
			throw new BizException("B_Oss_FileSizeExceeding100M", "文件大小不能超过100M");
		}
	}

	public void checkExt() {
		if (!fileFormatEnum.getExtNames().contains(extName)) {
			throw new BizException("B_Oss_FileExtFormatError", "文件扩展格式错误");
		}
	}

	public void getOssInfo(Supplier<OssUploadV> info1, Supplier<OssUploadV> info2) {
		OssUploadV ossUploadV = info1.get();
		if (ObjectUtils.isNull(ossUploadV)) {
			ossUploadV = info2.get();
			this.id = idGenerator.getId();
			this.url = ossUploadV.url();
			this.ossId = ossUploadV.id();
			this.publishEvent = true;
		} else {
			this.url = ossUploadV.url();
			this.id = ossUploadV.id();
		}
	}

}
