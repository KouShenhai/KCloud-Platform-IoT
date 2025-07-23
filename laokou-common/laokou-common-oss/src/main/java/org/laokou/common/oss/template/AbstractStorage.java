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

import org.laokou.common.oss.model.FileInfo;
import java.io.IOException;

/**
 * @author laokou
 */
public abstract class AbstractStorage<O> implements Storage {

	protected final FileInfo fileInfo;

	protected AbstractStorage(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}

	@Override
	public String uploadOss() throws Exception {
		O obj = getObj();
		checkBucket(obj);
		upload(obj);
		return getUrl(obj);
	}

	protected abstract O getObj() throws IOException;

	protected abstract void checkBucket(O obj) throws Exception;

	protected abstract void upload(O obj) throws Exception;

	protected abstract String getUrl(O obj) throws Exception;

}
