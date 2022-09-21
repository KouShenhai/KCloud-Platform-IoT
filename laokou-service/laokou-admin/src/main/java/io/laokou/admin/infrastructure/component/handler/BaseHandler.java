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
package io.laokou.admin.infrastructure.component.handler;

import io.laokou.admin.interfaces.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public abstract class BaseHandler implements Handler{

    @Autowired
    private HandleHolder handleHolder;

    protected Integer channelCode;

    @PostConstruct
    public void init() {
        handleHolder.putHandler(channelCode,this);
    }

    @Override
    public void doHandler(MessageDTO dto) {
        handler(dto);
    }

    public abstract boolean handler(MessageDTO dto);

}
