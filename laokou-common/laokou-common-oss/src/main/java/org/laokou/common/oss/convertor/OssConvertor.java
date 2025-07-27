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

package org.laokou.common.oss.convertor;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.oss.model.AmazonS3;
import org.laokou.common.oss.model.Local;
import org.laokou.common.oss.model.MinIO;

/**
 * @author laokou
 */
public final class OssConvertor {

	private OssConvertor() {
	}

	public static AmazonS3 toAmazonS3(Long id, String name, String param) throws JsonProcessingException {
		AmazonS3 amazonS3 = JacksonUtils.toBean(param, AmazonS3.class);
		amazonS3.setName(name);
		amazonS3.setId(id);
		return amazonS3;
	}

	public static Local toLocal(Long id, String name, String param) throws JsonProcessingException {
		Local local = JacksonUtils.toBean(param, Local.class);
		local.setName(name);
		local.setId(id);
		return local;
	}

	public static MinIO toMinIO(Long id, String name, String param) throws JsonProcessingException {
		MinIO minIO = JacksonUtils.toBean(param, MinIO.class);
		minIO.setName(name);
		minIO.setId(id);
		return minIO;
	}

}
