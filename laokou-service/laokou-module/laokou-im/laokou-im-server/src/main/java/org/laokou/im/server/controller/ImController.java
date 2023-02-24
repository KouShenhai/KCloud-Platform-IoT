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

package org.laokou.im.server.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.core.HttpResult;
import org.laokou.im.client.PushMsgDTO;
import org.laokou.im.server.service.ImService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author laokou
 */
@RestController
@Tag(name = "Im API",description = "即时通讯API")
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImController {

    private final ImService imService;

    @PostMapping("/push")
    @Operation(summary = "即时通讯>推送",description = "即时通讯>推送")
    public HttpResult<Boolean> push(@RequestBody PushMsgDTO dto) throws IOException {
        return new HttpResult<Boolean>().ok(imService.pusMessage(dto));
    }

}
