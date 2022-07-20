package io.laokou.admin.application.service;

import io.laokou.admin.interfaces.dto.MessageDTO;

public interface SysMessageApplicationService {

    Boolean pushMessage(MessageDTO dto);

    void consumeMessage(MessageDTO dto);

    Boolean insertMessage(MessageDTO dto);

}
