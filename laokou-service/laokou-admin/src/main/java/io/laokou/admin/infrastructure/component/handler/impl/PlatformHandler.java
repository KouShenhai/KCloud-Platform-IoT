package io.laokou.admin.infrastructure.component.handler.impl;

import io.laokou.admin.infrastructure.common.enums.ChannelTypeEnum;
import io.laokou.admin.infrastructure.component.event.PushMessageEvent;
import io.laokou.admin.infrastructure.component.handler.BaseHandler;
import io.laokou.admin.interfaces.dto.MessageDTO;
import io.laokou.common.utils.SpringContextUtil;
import org.springframework.stereotype.Service;

@Service
public class PlatformHandler extends BaseHandler {

    public PlatformHandler() {
        channelCode = ChannelTypeEnum.PLATFORM.ordinal();
    }

    @Override
    public boolean handler(MessageDTO dto) {
        SpringContextUtil.publishEvent(new PushMessageEvent(dto));
        return true;
    }
}
