package io.laokou.admin.interfaces.controller;

import io.laokou.admin.application.service.FlowableDefinitionApplicationService;
import io.laokou.common.utils.HttpResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/6 0006 下午 5:59
 */
@RestController
@Api(value = "流程定义API",protocols = "http",tags = "流程定义API")
@RequestMapping("/flowable/definition/api")
public class FlowableDefinitionApiController {

    @Autowired
    private FlowableDefinitionApplicationService flowableDefinitionApplicationService;

    @PostMapping("/insert")
    @ApiOperation("流程定义>新增")
    public HttpResultUtil<Boolean> insert(@RequestParam("name")String name, @RequestPart("file") MultipartFile file) throws IOException {
        return new HttpResultUtil<Boolean>().ok(flowableDefinitionApplicationService.importFile(name, file.getInputStream()));
    }

    @PostMapping("/query")
    @ApiOperation("流程定义>分页")
    public void query() {
        flowableDefinitionApplicationService.query();
    }

}
