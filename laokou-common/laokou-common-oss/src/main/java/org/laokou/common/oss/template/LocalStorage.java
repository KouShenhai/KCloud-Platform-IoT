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

package org.laokou.common.oss.template;

import org.laokou.common.core.util.FileUtils;
import org.laokou.common.core.util.UUIDGenerator;
import org.laokou.common.oss.model.BaseOss;
import org.laokou.common.oss.model.FileInfo;
import org.laokou.common.oss.model.Local;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

/**
 * @author laokou
 */
public class LocalStorage extends AbstractStorage<Path> {

	public LocalStorage(FileInfo fileInfo, BaseOss baseOss) {
		super(fileInfo, baseOss);
	}

	@Override
	protected Path getObj() throws IOException {
		Local local = getLocal();
		return FileUtils.create(local.getPath() + local.getDirectory(), getFileName());
	}

	@Override
	protected void createBucket(Path path) {
	}

	@Override
	protected void upload(Path path) throws NoSuchAlgorithmException {
		FileUtils.write(path, fileInfo.inputStream(), fileInfo.size());
	}

	@Override
	protected String getUrl(Path path) {
		Local local = getLocal();
		return local.getDomain() + local.getDirectory() + getFileName();
	}

	private String getFileName() {
		return UUIDGenerator.generateUUID() + fileInfo.extName();
	}

	private Local getLocal() {
		if (baseOss instanceof Local local) {
			return local;
		}
		throw new IllegalArgumentException("BaseOss must be an instance of Local");
	}

}
