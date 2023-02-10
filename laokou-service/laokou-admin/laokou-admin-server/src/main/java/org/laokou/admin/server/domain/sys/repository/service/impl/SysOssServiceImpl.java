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

package org.laokou.admin.server.domain.sys.repository.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.laokou.admin.server.domain.sys.entity.SysOssDO;
import org.laokou.admin.server.domain.sys.repository.mapper.SysOssMapper;
import org.laokou.admin.server.domain.sys.repository.service.SysOssService;
import org.laokou.admin.server.interfaces.qo.SysOssQo;
import org.laokou.oss.client.vo.SysOssVO;
import org.springframework.stereotype.Service;

/**
 * @author laokou
 */
@Service
public class SysOssServiceImpl extends ServiceImpl<SysOssMapper, SysOssDO> implements SysOssService {
    @Override
    public Boolean deleteOss(Long id) {
        this.baseMapper.deleteById(id);
        return true;
    }

    @Override
    public Integer getVersion(Long id) {
        return this.baseMapper.getVersion(id);
    }

    @Override
    public IPage<SysOssVO> queryOssPage(IPage<SysOssVO> page, SysOssQo qo) {
        return this.baseMapper.queryOssPage(page,qo);
    }

    @Override
    public SysOssVO getOssById(Long id) {
        return this.baseMapper.getOssById(id);
    }
}
