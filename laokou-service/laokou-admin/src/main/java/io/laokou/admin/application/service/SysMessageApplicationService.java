package io.laokou.admin.application.service;

import io.laokou.admin.interfaces.dto.MessageDTO;

import javax.servlet.http.HttpServletRequest;

public interface SysMessageApplicationService {

    Boolean pushMessage(MessageDTO dto);

    Boolean sendMessage(MessageDTO dto, HttpServletRequest request);

    void consumeMessage(MessageDTO dto);

    Boolean insertMessage(MessageDTO dto);

}
