package io.laokou.admin.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.qo.TaskQO;
import io.laokou.admin.interfaces.vo.TaskVO;

import javax.servlet.http.HttpServletRequest;

public interface WorkflowProcessApplicationService {

    Boolean startProcess(String processKey);

    IPage<TaskVO> queryTaskPage(TaskQO qo, HttpServletRequest request);

}
