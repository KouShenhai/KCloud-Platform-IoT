/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
package io.laokou.admin.infrastructure.component.listener;
import io.laokou.admin.application.service.SysMessageApplicationService;
import io.laokou.admin.infrastructure.component.event.PushMessageEvent;
import io.laokou.admin.infrastructure.component.event.SaveMessageEvent;
import io.laokou.admin.infrastructure.component.event.SendMessageEvent;
import io.laokou.admin.interfaces.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@ConditionalOnWebApplication
public class MessageListener {

    @Autowired
    private SysMessageApplicationService sysMessageApplicationService;

    @Async
    @Order
    @EventListener(value = SaveMessageEvent.class)
    public void listenSave(SaveMessageEvent event) {
        MessageDTO dto = (MessageDTO) event.getSource();
        sysMessageApplicationService.insertMessage(dto);
    }

    @Async
    @Order
    @EventListener(value = PushMessageEvent.class)
    public void listenPush(PushMessageEvent event) throws IOException {
        MessageDTO dto = (MessageDTO) event.getSource();
        sysMessageApplicationService.pushMessage(dto);
    }

    @Async
    @Order
    @EventListener(value = SendMessageEvent.class)
    public void listenSend(SendMessageEvent event) {
        MessageDTO dto = (MessageDTO) event.getSource();
        sysMessageApplicationService.consumeMessage(dto);
    }

}
