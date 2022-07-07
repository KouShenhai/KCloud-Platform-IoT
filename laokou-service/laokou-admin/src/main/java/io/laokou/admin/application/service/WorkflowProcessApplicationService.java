package io.laokou.admin.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.qo.TodoQO;
import io.laokou.admin.interfaces.vo.TodoVO;

import javax.servlet.http.HttpServletRequest;

public interface WorkflowProcessApplicationService {

    Boolean startProcess(String definitionId);

    IPage<TodoVO> todoTaskPage(TodoQO qo, HttpServletRequest request);

}
