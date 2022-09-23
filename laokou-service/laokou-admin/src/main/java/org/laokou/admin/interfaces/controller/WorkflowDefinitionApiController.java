/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
 */
package org.laokou.admin.interfaces.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.laokou.admin.application.service.WorkflowDefinitionApplicationService;
import org.laokou.admin.interfaces.qo.DefinitionQO;
import org.laokou.admin.interfaces.vo.DefinitionVO;
import org.laokou.common.enums.DataTypeEnum;
import org.laokou.common.utils.HttpResultUtil;
import org.laokou.log.annotation.OperateLog;
import org.laokou.security.annotation.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/6 0006 下午 5:59
 */
@RestController
@Api(value = "流程定义API",protocols = "http",tags = "流程定义API")
@RequestMapping("/workflow/definition/api")
public class WorkflowDefinitionApiController {

    @Autowired
    private WorkflowDefinitionApplicationService workflowDefinitionApplicationService;

    @PostMapping("/insert")
    @PreAuthorize("workflow:definition:insert")
    @ApiOperation("流程定义>新增")
    @OperateLog(module = "流程定义",name = "流程新增",type = DataTypeEnum.FILE)
    public HttpResultUtil<Boolean> insert(@RequestParam("name")String name, @RequestPart("file") MultipartFile file) throws IOException {
        return new HttpResultUtil<Boolean>().ok(workflowDefinitionApplicationService.importFile(name, file.getInputStream()));
    }

    @PostMapping("/query")
    @PreAuthorize("workflow:definition:query")
    @ApiOperation("流程定义>查询")
    public HttpResultUtil<IPage<DefinitionVO>> query(@RequestBody DefinitionQO qo) {
        return new HttpResultUtil<IPage<DefinitionVO>>().ok(workflowDefinitionApplicationService.queryDefinitionPage(qo));
    }

    @GetMapping("/image")
    @ApiOperation("流程定义>图片")
    @PreAuthorize("workflow:definition:image")
    public void image(@RequestParam("definitionId")String definitionId, HttpServletResponse response) {
        workflowDefinitionApplicationService.imageProcess(definitionId,response);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("workflow:definition:delete")
    @ApiOperation("流程定义>删除")
    @OperateLog(module = "流程定义",name = "流程删除")
    public HttpResultUtil<Boolean> delete(@RequestParam("deploymentId")String deploymentId) {
        return new HttpResultUtil<Boolean>().ok(workflowDefinitionApplicationService.deleteDefinition(deploymentId));
    }

    @PutMapping("/suspend")
    @ApiOperation("流程定义>挂起")
    @PreAuthorize("workflow:definition:suspend")
    @OperateLog(module = "流程定义",name = "流程挂起")
    public HttpResultUtil<Boolean> suspend(@RequestParam("definitionId")String definitionId) {
        return new HttpResultUtil<Boolean>().ok(workflowDefinitionApplicationService.suspendDefinition(definitionId));
    }

    @PutMapping("/activate")
    @ApiOperation("流程定义>激活")
    @PreAuthorize("workflow:definition:activate")
    @OperateLog(module = "流程定义",name = "流程激活")
    public HttpResultUtil<Boolean> activate(@RequestParam("definitionId")String definitionId) {
        return new HttpResultUtil<Boolean>().ok(workflowDefinitionApplicationService.activateDefinition(definitionId));
    }

}
