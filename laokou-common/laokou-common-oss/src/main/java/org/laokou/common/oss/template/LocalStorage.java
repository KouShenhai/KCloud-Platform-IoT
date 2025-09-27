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
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.oss.model.BaseOss;
import org.laokou.common.oss.model.FileInfo;
import org.laokou.common.oss.model.Local;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

/**
 * @author laokou
 */
public final class LocalStorage extends AbstractStorage<Path> {

	private final Local local;

	public LocalStorage(FileInfo fileInfo, BaseOss baseOss) {
		super(fileInfo);
		this.local = (Local) baseOss;
	}

	@Override
	protected Path getObj() throws IOException {
		return FileUtils.create(this.local.getPath() + this.local.getDirectory(), fileInfo.name());
	}

	@Override
	protected void checkBucket(Path path) {
	}

	@Override
	protected void upload(Path path) throws NoSuchAlgorithmException {
		FileUtils.write(path, fileInfo.inputStream(), fileInfo.size());
	}

	@Override
	protected String getUrl(Path path) {
		return local.getDomain() + this.local.getDirectory() + StringConstants.SLASH + fileInfo.name();
	}

	@Override
	public void createBucket() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteBucket() {
		throw new UnsupportedOperationException();
	}

}
