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

import org.laokou.common.oss.model.FileInfo11;
import org.laokou.common.oss.model.OssInfo;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @author laokou
 */
public abstract class AbstractStorage<O> implements Storage {

	protected final FileInfo11 fileInfo11;

	protected final OssInfo ossInfo;

	protected AbstractStorage(FileInfo11 fileInfo11, OssInfo ossInfo) {
		this.fileInfo11 = fileInfo11;
		this.ossInfo = ossInfo;
	}

	@Override
	public String uploadOss() throws IOException, NoSuchAlgorithmException {
		O obj = getObj();
		createBucket(obj);
		upload(obj);
		return getUrl(obj);
	}

	protected abstract O getObj() throws IOException;

	protected abstract void createBucket(O obj);

	protected abstract void upload(O obj) throws IOException, NoSuchAlgorithmException;

	protected abstract String getUrl(O obj);

}
