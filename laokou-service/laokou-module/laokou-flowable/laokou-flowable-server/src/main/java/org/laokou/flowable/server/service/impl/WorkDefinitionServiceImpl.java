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

package org.laokou.flowable.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.utils.Base64;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.impl.util.io.InputStreamSource;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.laokou.flowable.dto.DefinitionDTO;
import org.laokou.flowable.vo.DefinitionVO;
import org.laokou.flowable.vo.PageVO;
import org.laokou.flowable.server.service.WorkDefinitionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WorkDefinitionServiceImpl implements WorkDefinitionService {

	private final RepositoryService repositoryService;

	private static final String BPMN_FILE_SUFFIX = ".bpmn";

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean insertDefinition(InputStream in) {
		BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
		InputStreamSource inputStreamSource = new InputStreamSource(in);
		BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(inputStreamSource, true, true);
		Process process = bpmnModel.getProcesses().stream().findFirst().get();
		String processId = process.getId();
		String processName = process.getName() + BPMN_FILE_SUFFIX;
		long count = repositoryService.createDeploymentQuery().deploymentKey(processId).count();
		if (count > 0) {
			throw new GlobalException("流程已存在，请重新上传");
		}
		repositoryService.createDeployment()
			.name(processName)
			.key(processId)
			.addBpmnModel(processName, bpmnModel)
			.deploy();
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public PageVO<DefinitionVO> queryDefinitionPage(DefinitionDTO dto) {
		ValidatorUtil.validateEntity(dto);
		Integer pageNum = dto.getPageNum();
		Integer pageSize = dto.getPageSize();
		String processName = dto.getProcessName();
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
			.latestVersion()
			.orderByProcessDefinitionKey()
			.asc();
		if (StringUtil.isNotEmpty(processName)) {
			processDefinitionQuery = processDefinitionQuery.processDefinitionNameLike("%" + processName + "%");
		}
		long total = processDefinitionQuery.count();
		int pageIndex = pageSize * (pageNum - 1);
		List<ProcessDefinition> definitionList = processDefinitionQuery.listPage(pageIndex, pageSize);
		List<DefinitionVO> voList = new ArrayList<>(definitionList.size());
		for (ProcessDefinition processDefinition : definitionList) {
			DefinitionVO vo = new DefinitionVO();
			vo.setDefinitionId(processDefinition.getId());
			vo.setProcessKey(processDefinition.getKey());
			vo.setProcessName(processDefinition.getName());
			vo.setDeploymentId(processDefinition.getDeploymentId());
			vo.setSuspended(processDefinition.isSuspended());
			voList.add(vo);
		}
		return new PageVO<>(voList, total);
	}

	@Override
	public String diagramDefinition(String definitionId) {
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
			return Base64.encodeBase64String(outputStream.toByteArray());
		}
		catch (IOException e) {
			log.error("错误信息：{}", e.getMessage());
			return "";
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean deleteDefinition(String deploymentId) {
		// true允许级联删除 不设置会导致数据库关联异常
		repositoryService.deleteDeployment(deploymentId, true);
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean suspendDefinition(String definitionId) {
		final ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
			.processDefinitionId(definitionId)
			.singleResult();
		if (processDefinition.isSuspended()) {
			throw new GlobalException("挂起失败，流程已挂起");
		}
		else {
			// 挂起
			repositoryService.suspendProcessDefinitionById(definitionId, true, null);
		}
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean activateDefinition(String definitionId) {
		final ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
			.processDefinitionId(definitionId)
			.singleResult();
		if (processDefinition.isSuspended()) {
			// 激活
			repositoryService.activateProcessDefinitionById(definitionId, true, null);
			return true;
		}
		else {
			throw new GlobalException("激活失败，流程已激活");
		}
	}

}
