/**
 * Copyright 2020-2022 Kou Shenhai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
