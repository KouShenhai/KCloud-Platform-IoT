/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.admin.server.application.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.laokou.admin.server.application.service.WorkflowDefinitionApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.server.infrastructure.feign.flowable.WorkDefinitionApiFeignClient;
import org.laokou.admin.server.interfaces.qo.DefinitionQo;
import org.laokou.common.core.utils.FileUtil;
import org.laokou.common.i18n.core.CustomException;
import org.laokou.common.i18n.core.HttpResult;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.laokou.flowable.client.dto.DefinitionDTO;
import org.laokou.flowable.client.vo.DefinitionVO;
import org.laokou.flowable.client.vo.PageVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * @author laokou
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class WorkflowDefinitionApplicationServiceImpl implements WorkflowDefinitionApplicationService {

    private final WorkDefinitionApiFeignClient workDefinitionApiFeignClient;

    @Override
    public Boolean insertDefinition(String name, MultipartFile file) {
        HttpResult<Boolean> result = workDefinitionApiFeignClient.insert(name,file);
        if (!result.success()) {
            throw new CustomException(result.getCode(), result.getMsg());
        }
        return true;
    }

    @Override
    public IPage<DefinitionVO> queryDefinitionPage(DefinitionQo qo) {
        ValidatorUtil.validateEntity(qo);
        Integer pageSize = qo.getPageSize();
        Integer pageNum = qo.getPageNum();
        IPage<DefinitionVO> page = new Page<>(pageNum,pageSize);
        String processName = qo.getProcessName();
        DefinitionDTO dto = new DefinitionDTO();
        dto.setPageNum(pageNum);
        dto.setPageSize(pageSize);
        dto.setProcessName(processName);
        HttpResult<PageVO<DefinitionVO>> result = workDefinitionApiFeignClient.query(dto);
        if (!result.success()) {
            throw new CustomException(result.getCode(), result.getMsg());
        }
        PageVO<DefinitionVO> definitionVOPageVO = Optional.ofNullable(result.getData()).orElseGet(PageVO::new);
        page.setRecords(definitionVOPageVO.getRecords());
        page.setTotal(Optional.ofNullable(definitionVOPageVO.getTotal()).orElse(0L));
        return page;
    }

    @Override
    public String diagramDefinition(String definitionId) {
        HttpResult<String> result = workDefinitionApiFeignClient.diagram(definitionId);
        if (!result.success()) {
            throw new CustomException(result.getCode(),result.getMsg());
        }
        return result.getData();
    }

    @Override
    public Boolean deleteDefinition(String deploymentId) {
        HttpResult<Boolean> result = workDefinitionApiFeignClient.delete(deploymentId);
        if (!result.success()) {
            throw new CustomException(result.getCode(), result.getMsg());
        }
        return true;
    }

    @Override
    public Boolean suspendDefinition(String definitionId) {
        HttpResult<Boolean> result = workDefinitionApiFeignClient.suspend(definitionId);
        if (!result.success()) {
            throw new CustomException(result.getCode(), result.getMsg());
        }
        return true;
    }

    @Override
    public Boolean activateDefinition(String definitionId) {
        HttpResult<Boolean> result = workDefinitionApiFeignClient.activate(definitionId);
        if (!result.success()) {
            throw new CustomException(result.getCode(), result.getMsg());
        }
        return true;
    }

    @Override
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=audit.bpmn20.xml");
        InputStream inputStream = this.getClass().getResourceAsStream("/process/template/audit.bpmn20.xml");
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.write(inputStream.readAllBytes(),outputStream);
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

}
