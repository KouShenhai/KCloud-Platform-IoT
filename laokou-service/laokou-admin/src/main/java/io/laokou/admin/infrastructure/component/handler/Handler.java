package io.laokou.admin.infrastructure.component.handler;

import io.laokou.admin.interfaces.dto.MessageDTO;

/**
 * 消息处理器
 */
public interface Handler {

    void doHandler(MessageDTO dto);

}
