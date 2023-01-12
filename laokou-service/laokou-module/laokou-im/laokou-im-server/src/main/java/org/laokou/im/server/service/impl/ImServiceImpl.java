/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.im.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.laokou.im.client.PushMsgDTO;
import org.laokou.im.server.config.WebSocketServer;
import org.laokou.im.server.service.ImService;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class ImServiceImpl implements ImService {

    private final WebSocketServer webSocketServer;

    @Override
    public Boolean pusMessage(PushMsgDTO dto) throws IOException {
        for (String receiver : dto.getReceiver()) {
            webSocketServer.sendMessages(dto.getMsg(), receiver);
        }
        return true;
    }
}
