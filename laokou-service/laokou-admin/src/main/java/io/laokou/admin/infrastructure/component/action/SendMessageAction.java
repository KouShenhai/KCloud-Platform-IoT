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
package io.laokou.admin.infrastructure.component.action;
import io.laokou.admin.infrastructure.component.event.SendMessageEvent;
import io.laokou.admin.infrastructure.component.pipeline.BusinessProcess;
import io.laokou.admin.interfaces.dto.MessageDTO;
import io.laokou.common.utils.SpringContextUtil;
import org.springframework.stereotype.Service;

@Service
public class SendMessageAction implements BusinessProcess<MessageDTO> {
    @Override
    public void process(MessageDTO dto) {
        SpringContextUtil.publishEvent(new SendMessageEvent(dto));
    }
}
