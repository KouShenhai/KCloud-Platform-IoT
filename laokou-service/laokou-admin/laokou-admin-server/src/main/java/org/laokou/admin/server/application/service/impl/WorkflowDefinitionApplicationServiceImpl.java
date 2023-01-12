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
import feign.FeignException;
import feign.Response;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.server.application.service.WorkflowDefinitionApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.server.infrastructure.feign.flowable.WorkDefinitionApiFeignClient;
import org.laokou.admin.server.interfaces.qo.DefinitionQo;
import org.laokou.common.swagger.exception.CustomException;
import org.laokou.common.swagger.utils.HttpResult;
import org.laokou.flowable.client.dto.DefinitionDTO;
import org.laokou.flowable.client.vo.DefinitionVO;
import org.laokou.flowable.client.vo.PageVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author laokou
 * @version 1.0
 * @date 2022/7/6 0006 下午 6:11
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class WorkflowDefinitionApplicationServiceImpl implements WorkflowDefinitionApplicationService {

    private final WorkDefinitionApiFeignClient workDefinitionApiFeignClient;

    @Override
    public Boolean insertDefinition(String name, MultipartFile file) {
        try {
            HttpResult<Boolean> result = workDefinitionApiFeignClient.insert(name,file);
            if (!result.success()) {
                throw new CustomException(result.getCode(), result.getMsg());
            }
        } catch (FeignException e) {
            log.error("错误信息:{}",e.getMessage());
        }
        return true;
    }

    @Override
    public IPage<DefinitionVO> queryDefinitionPage(DefinitionQo qo) {
        Integer pageSize = qo.getPageSize();
        Integer pageNum = qo.getPageNum();
        IPage<DefinitionVO> page = new Page<>(pageNum,pageSize);
        try {
            String processName = qo.getProcessName();
            DefinitionDTO dto = new DefinitionDTO();
            dto.setPageNum(pageNum);
            dto.setPageSize(pageSize);
            dto.setProcessName(processName);
            HttpResult<PageVO<DefinitionVO>> result = workDefinitionApiFeignClient.query(dto);
            if (!result.success()) {
                throw new CustomException(result.getCode(), result.getMsg());
            }
            page.setRecords(result.getData().getRecords());
            page.setTotal(result.getData().getTotal());
        } catch (FeignException e) {
            log.error("错误信息:{}",e.getMessage());
        }
        return page;
    }

    @Override
    public void diagramDefinition(String definitionId, HttpServletResponse response) {
        try (
                Response result = workDefinitionApiFeignClient.diagram(definitionId);
                InputStream inputStream = result.body().asInputStream();
                OutputStream outputStream = response.getOutputStream();
        ) {
            byte[] bytes = new byte[inputStream.available()];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean deleteDefinition(String deploymentId) {
        try {
            HttpResult<Boolean> result = workDefinitionApiFeignClient.delete(deploymentId);
            if (!result.success()) {
                throw new CustomException(result.getCode(), result.getMsg());
            }
        } catch (FeignException e) {
            log.error("错误信息:{}",e.getMessage());
        }
        return true;
    }

    @Override
    public Boolean suspendDefinition(String definitionId) {
        try {
            HttpResult<Boolean> result = workDefinitionApiFeignClient.suspend(definitionId);
            if (!result.success()) {
                throw new CustomException(result.getCode(), result.getMsg());
            }
        } catch (FeignException e) {
            log.error("错误信息:{}",e.getMessage());
        }
        return true;
    }

    @Override
    public Boolean activateDefinition(String definitionId) {
        try {
            HttpResult<Boolean> result = workDefinitionApiFeignClient.activate(definitionId);
            if (!result.success()) {
                throw new CustomException(result.getCode(), result.getMsg());
            }
        } catch (FeignException e) {
            log.error("错误信息:{}",e.getMessage());
        }
        return true;
    }

}
