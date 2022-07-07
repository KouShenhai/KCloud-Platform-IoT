package io.laokou.admin.application.service;

import io.laokou.admin.interfaces.dto.AuditDTO;

import javax.servlet.http.HttpServletRequest;

public interface WorkflowTaskApplicationService {

    Boolean auditTask(AuditDTO dto, HttpServletRequest request);

}
