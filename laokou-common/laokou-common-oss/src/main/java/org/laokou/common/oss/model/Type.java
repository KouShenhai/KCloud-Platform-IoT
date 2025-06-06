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

import lombok.Getter;
import org.laokou.common.i18n.util.EnumParser;
import org.laokou.common.oss.template.AmazonS3Storage;
import org.laokou.common.oss.template.LocalStorage;
import org.laokou.common.oss.template.Storage;

@Getter
public enum Type {

	LOCAL("local", "本地") {
		@Override
		public Storage getStorage(FileInfo fileInfo, OssInfo ossInfo) {
			return new LocalStorage(fileInfo, ossInfo);
		}
	},

	CLOUD("cloud", "云端") {
		@Override
		public Storage getStorage(FileInfo fileInfo, OssInfo ossInfo) {
			return new AmazonS3Storage(fileInfo, ossInfo);
		}
	};

	private final String code;

	private final String desc;

	Type(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static Type getByCode(String code) {
		return EnumParser.parse(Type.class, Type::getCode, code);
	}

	public abstract Storage getStorage(FileInfo fileInfo, OssInfo ossInfo);

}
