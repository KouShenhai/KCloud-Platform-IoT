package io.laokou.admin.application.service;

import java.io.InputStream;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/6 0006 下午 6:10
 */
public interface FlowableDefinitionApplicationService {

    String BPMN_FILE_SUFFIX = ".bpmn";

    Boolean importFile(String name, InputStream in);

    void query();

}
