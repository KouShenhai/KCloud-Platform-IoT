package io.laokou.admin.application.service;

import io.laokou.admin.interfaces.dto.AuditDTO;
import io.laokou.admin.interfaces.dto.ClaimDTO;
import io.laokou.admin.interfaces.dto.UnClaimDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface WorkflowTaskApplicationService {

    Boolean auditTask(AuditDTO dto, HttpServletRequest request);

    Boolean claimTask(ClaimDTO dto,HttpServletRequest request);

    Boolean unClaimTask(UnClaimDTO dto);

    Boolean deleteTask(String taskId);

    void diagramProcess(String processInstanceId, HttpServletResponse response) throws IOException;
}
