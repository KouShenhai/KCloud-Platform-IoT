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

package org.laokou.common.oss.entity;

import lombok.Data;
import org.laokou.common.core.utils.FileUtil;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.UUID;

/**
 * @author laokou
 */
@Data
public class FileInfo implements Serializable {

	protected final long size;

	protected final String name;

	protected InputStream inputStream;

	protected final String contentType;

	protected final long chunkSize;

	public FileInfo(MultipartFile file) throws IOException {
		this.inputStream = file.getInputStream();
		this.size = file.getSize();
		this.name = UUID.randomUUID() + FileUtil.getFileExt(file.getOriginalFilename());
		this.contentType = file.getContentType();
		this.chunkSize = Math.max(this.size / 100, 8192);
	}

}
