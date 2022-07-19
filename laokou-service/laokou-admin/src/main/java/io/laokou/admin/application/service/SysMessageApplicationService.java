package io.laokou.admin.application.service;

import io.laokou.admin.interfaces.dto.MessageDTO;

public interface SysMessageApplicationService {

    Boolean send(MessageDTO dto);

}
