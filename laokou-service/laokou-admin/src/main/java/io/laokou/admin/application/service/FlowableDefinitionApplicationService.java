package io.laokou.admin.application.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.qo.DefinitionQO;
import io.laokou.admin.interfaces.vo.DefinitionVO;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/6 0006 下午 6:10
 */
public interface FlowableDefinitionApplicationService {

    String BPMN_FILE_SUFFIX = ".bpmn";

    Boolean importFile(String name, InputStream in);

    IPage<DefinitionVO> queryDefinitionPage(DefinitionQO qo);

    void imageProcess(String definitionId, HttpServletResponse response);

    Boolean deleteDefinition(String deploymentId);

}
