package io.laokou.admin.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.dto.AuditDTO;
import io.laokou.admin.interfaces.qo.TaskQO;
import io.laokou.admin.interfaces.vo.StartProcessVO;
import io.laokou.admin.interfaces.vo.TaskVO;

import javax.servlet.http.HttpServletRequest;

public interface WorkflowProcessApplicationService {

    StartProcessVO startResourceProcess(String processKey,String businessKey,String instanceName);

    IPage<TaskVO> queryResourceTaskPage(TaskQO qo, HttpServletRequest request);

    Boolean auditResourceTask(AuditDTO dto,HttpServletRequest request);
}
