/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.flowable.domain.definition;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.SneakyThrows;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author laokou
 */
@Data
@Schema(name = "Deployment", description = "部署")
public class Deployment extends AggregateRoot<Long> {

	private static final String BPMN_FILE_SUFFIX = ".bpmn";

	@Schema(name = "key", description = "键")
	private InputStream inputStream;

	@Schema(name = "inputStream", description = "输入流")
	private String key;

	@Schema(name = "name", description = "名称")
	private String name;

	@SneakyThrows
	public Deployment(MultipartFile file) {
		this.inputStream = file.getInputStream();
	}

	public void modify(String id, String name) {
		this.key = id;
		this.name = name + BPMN_FILE_SUFFIX;
	}

	public void checkKey(long count) {
		if (count > 0) {
			throw new RuntimeException("流程已存在，请更换流程图并上传");
		}
	}

}
