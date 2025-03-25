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
import org.laokou.common.oss.entity.FileInfo;
import org.laokou.common.oss.entity.OssInfo;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

/**
 * @author laokou
 */
public class LocalStorage extends AbstractStorage<Path> {

	public LocalStorage(FileInfo fileInfo, OssInfo ossInfo) {
		super(fileInfo, ossInfo);
	}

	@Override
	protected Path getObj() throws IOException {
		return FileUtils.create(ossInfo.getDirectory(), fileInfo.getName());
	}

	@Override
	protected void createBucket(Path obj) {
	}

	@Override
	protected void upload(Path obj) throws NoSuchAlgorithmException {
		FileUtils.write(obj, fileInfo.getInputStream(), fileInfo.getSize());
	}

	@Override
	protected String getUrl(Path obj) {
		return ossInfo.getDomain() + ossInfo.getPath() + fileInfo.getName();
	}

}
