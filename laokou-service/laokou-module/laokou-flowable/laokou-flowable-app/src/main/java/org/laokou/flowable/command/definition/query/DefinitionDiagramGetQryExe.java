/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.flowable.command.definition.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.utils.Base64;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.RepositoryService;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.laokou.common.i18n.dto.Result;
import org.laokou.flowable.dto.definition.DefinitionDiagramGetQry;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DefinitionDiagramGetQryExe {

	private final RepositoryService repositoryService;

	public Result<String> execute(DefinitionDiagramGetQry qry) {
		String definitionId = qry.getDefinitionId();
		// 获取图片流
		DefaultProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionId);
		// 输出为图片
		InputStream inputStream = diagramGenerator.generateDiagram(bpmnModel, "png", Collections.emptyList(),
				Collections.emptyList(), "宋体", "宋体", "宋体", null, 1.0, false);
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			BufferedImage image = ImageIO.read(inputStream);
			if (null != image) {
				ImageIO.write(image, "png", outputStream);
			}
			return Result.of(Base64.encodeBase64String(outputStream.toByteArray()));
		}
		catch (IOException e) {
			log.error("错误信息：{}", e.getMessage());
			return Result.of("");
		}
	}

}
