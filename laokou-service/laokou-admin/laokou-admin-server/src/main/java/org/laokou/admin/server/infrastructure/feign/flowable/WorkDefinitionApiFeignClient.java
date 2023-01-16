package org.laokou.admin.server.infrastructure.feign.flowable;
import feign.Response;
import org.laokou.admin.server.infrastructure.feign.flowable.factory.WorkDefinitionApiFeignClientFallbackFactory;
import org.laokou.common.core.constant.ServiceConstant;
import org.laokou.common.swagger.utils.HttpResult;
import org.laokou.flowable.client.dto.DefinitionDTO;
import org.laokou.flowable.client.vo.DefinitionVO;
import org.laokou.flowable.client.vo.PageVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author laokou
 */
@FeignClient(contextId = "workDefinition",value = ServiceConstant.LAOKOU_FLOWABLE,path = "/work/definition/api", fallbackFactory = WorkDefinitionApiFeignClientFallbackFactory.class)
@Service
public interface WorkDefinitionApiFeignClient {

    /**
     * 新增流程
     * @param name
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/insert",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    HttpResult<Boolean> insert(@RequestParam("name")String name, @RequestPart("file") MultipartFile file);

    /**
     * 查询流程
     * @param dto
     * @return
     */
    @PostMapping(value = "/query")
    HttpResult<PageVO<DefinitionVO>> query(@RequestBody DefinitionDTO dto);

    /**
     * 流程图
     * @param definitionId
     * @return
     */
    @GetMapping(value = "/diagram")
    Response diagram(@RequestParam("definitionId")String definitionId);

    /**
     * 删除流程
     * @param deploymentId
     * @return
     */
    @DeleteMapping(value = "/delete")
    HttpResult<Boolean> delete(@RequestParam("deploymentId")String deploymentId);

    /**
     * 挂起流程
     * @param definitionId
     * @return
     */
    @PutMapping(value = "/suspend")
    HttpResult<Boolean> suspend(@RequestParam("definitionId")String definitionId);

    /**
     * 激活流程
     * @param definitionId
     * @return
     */
    @PutMapping(value = "/activate")
    HttpResult<Boolean> activate(@RequestParam("definitionId")String definitionId);

}
