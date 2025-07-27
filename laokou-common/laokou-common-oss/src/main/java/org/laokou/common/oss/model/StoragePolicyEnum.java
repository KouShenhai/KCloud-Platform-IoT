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

package org.laokou.common.oss.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import org.laokou.common.i18n.util.EnumParser;
import org.laokou.common.oss.convertor.OssConvertor;
import org.laokou.common.oss.template.AmazonS3Storage;
import org.laokou.common.oss.template.LocalStorage;
import org.laokou.common.oss.template.MinIOStorage;
import org.laokou.common.oss.template.Storage;

@Getter
public enum StoragePolicyEnum {

	LOCAL("local", "本地") {
		@Override
		public BaseOss getOss(Long id, String name, String param) throws JsonProcessingException {
			return OssConvertor.toLocal(id, name, param);
		}

		@Override
		public Storage getStorage(FileInfo fileInfo, BaseOss baseOss) {
			return new LocalStorage(fileInfo, baseOss);
		}
	},

	AMAZON_S3("amazon_s3", "亚马逊S3") {
		@Override
		public BaseOss getOss(Long id, String name, String param) throws JsonProcessingException {
			return OssConvertor.toAmazonS3(id, name, param);
		}

		@Override
		public Storage getStorage(FileInfo fileInfo, BaseOss baseOss) {
			return new AmazonS3Storage(fileInfo, baseOss);
		}
	},

	MINIO("minio", "MinIO") {
		@Override
		public BaseOss getOss(Long id, String name, String param) throws JsonProcessingException {
			return OssConvertor.toMinIO(id, name, param);
		}

		@Override
		public Storage getStorage(FileInfo fileInfo, BaseOss baseOss) {
			return new MinIOStorage(fileInfo, baseOss);
		}
	};

	private final String code;

	private final String desc;

	StoragePolicyEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static StoragePolicyEnum getByCode(String code) {
		return EnumParser.parse(StoragePolicyEnum.class, StoragePolicyEnum::getCode, code);
	}

	public abstract BaseOss getOss(Long id, String name, String param) throws JsonProcessingException;

	public abstract Storage getStorage(FileInfo fileInfo, BaseOss baseOss);

}
