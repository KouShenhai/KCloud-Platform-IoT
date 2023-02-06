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
import org.laokou.admin.server.domain.sys.entity.SysDictDO;
import org.laokou.admin.server.domain.sys.repository.mapper.SysDictMapper;
import org.laokou.admin.server.domain.sys.repository.service.SysDictService;
import org.laokou.admin.server.interfaces.qo.SysDictQo;
import org.laokou.admin.client.vo.SysDictVO;
import org.springframework.stereotype.Service;
/**
 * @author laokou
 * @version 1.0
 * @date 2022/6/23 0023 上午 11:03
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDictDO> implements SysDictService {

    @Override
    public IPage<SysDictVO> getDictList(IPage<SysDictVO> page, SysDictQo qo) {
        return this.baseMapper.getDictList(page,qo);
    }

    @Override
    public SysDictVO getDictById(Long id) {
        return this.baseMapper.getDictById(id);
    }

    @Override
    public void deleteDict(Long id) {
        this.baseMapper.deleteById(id);
    }

    @Override
    public Integer getVersion(Long id) {
        return this.baseMapper.getVersion(id);
    }

}
