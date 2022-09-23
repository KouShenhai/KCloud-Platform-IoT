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
package org.laokou.admin.domain.sys.repository.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.laokou.admin.domain.sys.entity.SysDictDO;
import org.laokou.admin.domain.sys.repository.mapper.SysDictMapper;
import org.laokou.admin.domain.sys.repository.service.SysDictService;
import org.laokou.admin.interfaces.qo.SysDictQO;
import org.laokou.admin.interfaces.vo.SysDictVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/6/23 0023 上午 11:03
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDictDO> implements SysDictService {

    @Override
    public List<SysDictVO> getDictList(SysDictQO qo) {
        return this.baseMapper.getDictList(qo);
    }

    @Override
    public IPage<SysDictVO> getDictList(IPage<SysDictVO> page, SysDictQO qo) {
        return this.baseMapper.getDictList(page,qo);
    }

    @Override
    public SysDictVO getDictById(Long id) {
        return this.baseMapper.getDictById(id);
    }

    @Override
    public void deleteDict(Long id) {
        this.baseMapper.deleteDict(id);
    }

}
