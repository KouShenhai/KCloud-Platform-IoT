package io.laokou.admin.application.service;

import io.laokou.admin.interfaces.dto.AuditDTO;

public interface WorkflowTaskApplicationService {

    Boolean auditTask(AuditDTO dto);

}
