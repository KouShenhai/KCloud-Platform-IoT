/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.admin.server.domain.sys.repository.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.laokou.admin.server.domain.sys.entity.SysMessageDO;
import org.laokou.admin.server.domain.sys.repository.mapper.SysMessageMapper;
import org.laokou.admin.server.domain.sys.repository.service.SysMessageService;
import org.laokou.admin.server.interfaces.qo.SysMessageQo;
import org.laokou.admin.client.vo.MessageDetailVO;
import org.laokou.admin.client.vo.SysMessageVO;
import org.springframework.stereotype.Service;
/**
 * @author laokou
 */
@Service
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessageDO> implements SysMessageService {
    @Override
    public IPage<SysMessageVO> getMessageList(IPage<SysMessageVO> page, SysMessageQo qo) {
        return this.baseMapper.getMessageList(page,qo);
    }

    @Override
    public MessageDetailVO getMessageByDetailId(Long id) {
        return this.baseMapper.getMessageByDetailId(id);
    }

    @Override
    public IPage<SysMessageVO> getUnReadList(IPage<SysMessageVO> page,Integer type, Long userId) {
        return this.baseMapper.getUnReadList(page,type,userId);
    }

    @Override
    public void readMessage(Long id) {
        this.baseMapper.readMessage(id);
    }

    @Override
    public MessageDetailVO getMessageById(Long id) {
        return this.baseMapper.getMessageById(id);
    }
}
