package io.laokou.admin.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.qo.TaskQO;
import io.laokou.admin.interfaces.vo.StartProcessVO;
import io.laokou.admin.interfaces.vo.TaskVO;

import javax.servlet.http.HttpServletRequest;

public interface WorkflowProcessApplicationService {

    StartProcessVO startProcess(String processKey,String businessKey,String instanceName);

    IPage<TaskVO> queryTaskPage(TaskQO qo, HttpServletRequest request);

}
