package org.laokou.flowable.server.service;
import jakarta.servlet.http.HttpServletResponse;
import org.laokou.flowable.client.dto.DefinitionDTO;
import org.laokou.flowable.client.vo.DefinitionVO;
import org.laokou.flowable.client.vo.PageVO;
import java.io.InputStream;
/**
 * @author laokou
 */
public interface WorkDefinitionService {

    /**
     * 新增流程文件
     * @param name
     * @param in
     * @return
     */
    Boolean insertDefinition(String name, InputStream in);

    /**
     * 分页查询流程
     * @param dto
     * @return
     */
    PageVO<DefinitionVO> queryDefinitionPage(DefinitionDTO dto);

    /**
     * 查看流程图
     * @param definitionId
     * @param response
     */
    void diagramDefinition(String definitionId, HttpServletResponse response);

    /**
     * 删除流程
     * @param deploymentId
     * @return
     */
    Boolean deleteDefinition(String deploymentId);

    /**
     * 挂起流程
     * @param definitionId
     * @return
     */
    Boolean suspendDefinition(String definitionId);

    /**
     * 激活流程
     * @param definitionId
     * @return
     */
    Boolean activateDefinition(String definitionId);

}
