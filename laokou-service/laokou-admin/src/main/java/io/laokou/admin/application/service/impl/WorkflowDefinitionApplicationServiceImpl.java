package io.laokou.admin.application.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.laokou.admin.application.service.WorkflowDefinitionApplicationService;
import io.laokou.admin.interfaces.qo.DefinitionQO;
import io.laokou.admin.interfaces.vo.DefinitionVO;
import io.laokou.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/6 0006 下午 6:11
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class WorkflowDefinitionApplicationServiceImpl implements WorkflowDefinitionApplicationService {

    @Autowired
    private RepositoryService repositoryService;

    @Override
    public Boolean importFile(String name, InputStream in) {
        String processName = name + BPMN_FILE_SUFFIX;
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
                .name(processName)
                .key(name)
                .addInputStream(processName, in);
        deploymentBuilder.deploy();
        return true;
    }

    @Override
    public IPage<DefinitionVO> queryDefinitionPage(DefinitionQO qo) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .orderByProcessDefinitionKey().asc();
        if (StringUtils.isNotBlank(qo.getProcessName())) {
            processDefinitionQuery = processDefinitionQuery.processDefinitionNameLike("%" + qo.getProcessName() + "%");
        }
        long pageTotal = processDefinitionQuery.count();
        Integer pageNum = qo.getPageNum();
        Integer pageSize = qo.getPageSize();
        IPage<DefinitionVO> page = new Page<>(pageNum,pageSize,pageTotal);
        int pageIndex = pageSize * (pageNum - 1);
        List<ProcessDefinition> definitionList = processDefinitionQuery.listPage(pageIndex, pageSize);
        List<DefinitionVO> definitions = Lists.newArrayList();
        for (ProcessDefinition processDefinition : definitionList) {
            DefinitionVO vo = new DefinitionVO();
            vo.setDefinitionId(processDefinition.getId());
            vo.setProcessKey(processDefinition.getKey());
            vo.setProcessName(processDefinition.getName());
            vo.setDeploymentId(processDefinition.getDeploymentId());
            vo.setSuspended(processDefinition.isSuspended());
            definitions.add(vo);
        }
        page.setRecords(definitions);
        return page;
    }

    @Override
    public void imageProcess(String definitionId, HttpServletResponse response) {
        //获取图片流
        DefaultProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionId);
        //输出为图片
        InputStream inputStream = diagramGenerator.generateDiagram(
                bpmnModel,
                "png",
                Collections.emptyList(),
                Collections.emptyList(),
                "宋体",
                "宋体",
                "宋体",
                null,
                1.0,
                false);
        try(ServletOutputStream os = response.getOutputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            if (null != image) {
                response.setHeader("Cache-Control", "no-store, no-cache");
                response.setContentType("image/png");
                ImageIO.write(image,"png",os);
            }
        } catch (IOException e) {
            log.error("错误信息：{}",e.getMessage());
        }
    }

    @Override
    public Boolean deleteDefinition(String deploymentId) {
        // true允许级联删除 不设置会导致数据库关联异常
        repositoryService.deleteDeployment(deploymentId,true);
        return true;
    }

    @Override
    public Boolean suspendDefinition(String definitionId) {
        final ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(definitionId).singleResult();
        if (processDefinition.isSuspended()) {
            throw new CustomException("挂起失败，流程已挂起");
        } else {
            // 挂起
            repositoryService.suspendProcessDefinitionById(definitionId, true, null);
        }
        return true;
    }

    @Override
    public Boolean activateDefinition(String definitionId) {
        final ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(definitionId).singleResult();
        if (processDefinition.isSuspended()) {
            // 激活
            repositoryService.activateProcessDefinitionById(definitionId, true, null);
        } else {
            throw new CustomException("激活失败，流程已激活");
        }
        return true;
    }

}
