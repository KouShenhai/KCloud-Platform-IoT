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
package io.laokou.admin.domain.sys.repository.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.sys.entity.SysMessageDO;
import io.laokou.admin.domain.sys.repository.mapper.SysMessageMapper;
import io.laokou.admin.domain.sys.repository.service.SysMessageService;
import io.laokou.admin.interfaces.qo.SysMessageQO;
import io.laokou.admin.interfaces.vo.MessageDetailVO;
import io.laokou.admin.interfaces.vo.SysMessageVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessageDO> implements SysMessageService {
    @Override
    public IPage<SysMessageVO> getMessageList(IPage<SysMessageVO> page, SysMessageQO qo) {
        return this.baseMapper.getMessageList(page,qo);
    }

    @Override
    public MessageDetailVO getMessageByDetailId(Long id) {
        return this.baseMapper.getMessageByDetailId(id);
    }

    @Override
    public IPage<SysMessageVO> getUnReadList(IPage<SysMessageVO> page, Long userId) {
        return this.baseMapper.getUnReadList(page,userId);
    }

    @Override
    public Boolean readMessage(Long id) {
        this.baseMapper.readMessage(id);
        return true;
    }

    @Override
    public MessageDetailVO getMessageById(Long id) {
        return this.baseMapper.getMessageById(id);
    }
}
