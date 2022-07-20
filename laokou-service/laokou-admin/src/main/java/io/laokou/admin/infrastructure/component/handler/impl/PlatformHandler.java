package io.laokou.admin.infrastructure.component.handler.impl;

import io.laokou.admin.infrastructure.common.enums.ChannelTypeEnum;
import io.laokou.admin.infrastructure.component.handler.BaseHandler;
import io.laokou.admin.infrastructure.config.WebsocketStompServer;
import io.laokou.admin.interfaces.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlatformHandler extends BaseHandler {

    @Autowired
    private WebsocketStompServer websocketStompServer;

    public PlatformHandler() {
        channelCode = ChannelTypeEnum.PLATFORM.ordinal();
    }

    @Override
    public boolean handler(MessageDTO dto) {
        return true;
    }
}
