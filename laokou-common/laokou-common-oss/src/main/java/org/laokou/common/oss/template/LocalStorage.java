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
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.oss.model.BaseOss;
import org.laokou.common.oss.model.FileInfo;
import org.laokou.common.oss.model.Local;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

import static org.laokou.common.i18n.common.constant.StringConstants.SLASH;

/**
 * @author laokou
 */
public final class LocalStorage extends AbstractStorage<Path> {

	private volatile Local local;

	private final Object lock = new Object();

	public LocalStorage(FileInfo fileInfo, BaseOss baseOss) {
		super(fileInfo, baseOss);
	}

	@Override
	protected Path getObj() throws IOException {
		Local local = getLocal();
		return FileUtils.create(local.getPath() + local.getDirectory(), fileInfo.name());
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
		return local.getDomain() + local.getDirectory() + SLASH + fileInfo.name();
	}

	private Local getLocal() {
		if (ObjectUtils.isNull(local)) {
			synchronized (lock) {
				if (ObjectUtils.isNull(local)) {
					if (baseOss instanceof Local l) {
						return local = l;
					}
					throw new IllegalArgumentException("BaseOss must be an instance of Local");
				}
			}
		}
		return local;
	}

}
