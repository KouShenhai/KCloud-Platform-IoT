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
import org.laokou.admin.client.dto.SysUserDTO;
import org.laokou.admin.server.interfaces.qo.SysUserQo;
import org.laokou.common.core.vo.OptionVO;
import org.laokou.admin.client.vo.SysUserVO;
import org.laokou.admin.server.domain.sys.entity.SysUserDO;
import org.laokou.admin.server.domain.sys.repository.mapper.SysUserMapper;
import org.laokou.admin.server.domain.sys.repository.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * @author laokou
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserDO> implements SysUserService {
    @Override
    public void updateUser(SysUserDTO dto) {
        this.baseMapper.updateUser(dto);
    }

    @Override
    public IPage<SysUserVO> getUserPage(IPage<SysUserVO> page, SysUserQo qo) {
        return this.baseMapper.getUserPage(page, qo);
    }

    @Override
    public void deleteUser(Long id) {
        this.baseMapper.deleteById(id);
    }

    @Override
    public List<OptionVO> getOptionList() {
        return this.baseMapper.getOptionList();
    }

    @Override
    public Integer getVersion(Long id) {
        return this.getVersion(id);
    }

}
