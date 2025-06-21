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

package org.laokou.oss.convertor;

import org.laokou.common.oss.model.FileInfo;
import org.laokou.oss.factory.OssDomainFactory;
import org.laokou.oss.model.FileFormatEnum;
import org.laokou.oss.model.OssA;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author laokou
 */
public final class OssConvertor {

	private OssConvertor() {
	}

	public static OssA toEntity(MultipartFile file, String fileType) {
		OssA oss = OssDomainFactory.getOss();
		oss.setFile(file);
		oss.setFileFormatEnum(FileFormatEnum.getByCode(fileType));
		return oss;
	}

	public static FileInfo to(OssA ossA) throws IOException {
		return new FileInfo(ossA.getInputStream(), ossA.getSize(), ossA.getContentType(), ossA.getName(),
				ossA.getExtName());
	}

}
