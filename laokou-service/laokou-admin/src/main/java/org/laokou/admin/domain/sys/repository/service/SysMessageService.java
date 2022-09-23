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
package org.laokou.admin.domain.sys.repository.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.laokou.admin.domain.sys.entity.SysMessageDO;
import org.laokou.admin.interfaces.qo.SysMessageQO;
import org.laokou.admin.interfaces.vo.MessageDetailVO;
import org.laokou.admin.interfaces.vo.SysMessageVO;

public interface SysMessageService extends IService<SysMessageDO> {

    IPage<SysMessageVO> getMessageList(IPage<SysMessageVO> page, SysMessageQO qo);

    MessageDetailVO getMessageByDetailId(Long id);

    IPage<SysMessageVO> getUnReadList(IPage<SysMessageVO> page, Long userId);

    Boolean readMessage(Long id);

    MessageDetailVO getMessageById(Long id);

}
