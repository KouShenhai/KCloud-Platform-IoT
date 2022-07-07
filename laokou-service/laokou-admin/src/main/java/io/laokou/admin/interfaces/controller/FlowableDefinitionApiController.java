package io.laokou.admin.interfaces.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.application.service.FlowableDefinitionApplicationService;
import io.laokou.admin.interfaces.qo.DefinitionQO;
import io.laokou.admin.interfaces.vo.DefinitionVO;
import io.laokou.common.utils.HttpResultUtil;
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
    public HttpResultUtil<IPage<DefinitionVO>> query(@RequestBody DefinitionQO qo) {
        return new HttpResultUtil<IPage<DefinitionVO>>().ok(flowableDefinitionApplicationService.queryDefinitionPage(qo));
    }

    @GetMapping("/image")
    @ApiOperation("流程定义>图片")
    public void image(@RequestParam("definitionId")String definitionId, HttpServletResponse response) {
        flowableDefinitionApplicationService.imageProcess(definitionId,response);
    }

    @DeleteMapping("/delete")
    @ApiOperation("流程定义>删除")
    public HttpResultUtil<Boolean> delete(@RequestParam("deploymentId")String deploymentId) {
        return new HttpResultUtil<Boolean>().ok(flowableDefinitionApplicationService.deleteDefinition(deploymentId));
    }

    @PutMapping("/suspend")
    @ApiOperation("流程定义>挂起")
    public HttpResultUtil<Boolean> suspend(@RequestParam("definitionId")String definitionId) {
        return new HttpResultUtil<Boolean>().ok(flowableDefinitionApplicationService.suspendDefinition(definitionId));
    }

    @PutMapping("/activate")
    @ApiOperation("流程定义>激活")
    public HttpResultUtil<Boolean> activate(@RequestParam("definitionId")String definitionId) {
        return new HttpResultUtil<Boolean>().ok(flowableDefinitionApplicationService.activateDefinition(definitionId));
    }

}
