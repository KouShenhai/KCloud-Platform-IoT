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
package io.laokou.admin.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.dto.MessageDTO;
import io.laokou.admin.interfaces.qo.SysMessageQO;
import io.laokou.admin.interfaces.vo.MessageDetailVO;
import io.laokou.admin.interfaces.vo.SysMessageVO;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface SysMessageApplicationService {

    Boolean pushMessage(MessageDTO dto) throws IOException;

    Boolean sendMessage(MessageDTO dto, HttpServletRequest request);

    void consumeMessage(MessageDTO dto);

    Boolean insertMessage(MessageDTO dto);

    IPage<SysMessageVO> queryMessagePage(SysMessageQO qo);

    MessageDetailVO getMessageByDetailId(Long id);

    MessageDetailVO getMessageById(Long id);

    IPage<SysMessageVO> getUnReadList(HttpServletRequest request, SysMessageQO qo);

    Integer unReadCount(HttpServletRequest request);

}
