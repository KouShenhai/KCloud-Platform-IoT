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
package org.laokou.common.log.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.session.ResultHandler;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.log.dto.LoginLogDTO;
import org.laokou.common.log.entity.SysLoginLogDO;
import org.laokou.common.log.mapper.SysLoginLogMapper;
import org.laokou.common.log.qo.SysLoginLogQo;
import org.laokou.common.log.service.SysLoginLogService;
import org.laokou.common.log.vo.SysLoginLogVO;
import org.springframework.stereotype.Service;
/**
 * @author laokou
 */
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper,SysLoginLogDO> implements SysLoginLogService {

    @Override
    public IPage<SysLoginLogVO> getLoginLogList(IPage<SysLoginLogVO> page, SysLoginLogQo qo) {
        return this.baseMapper.getLoginLogList(page,qo);
    }

    @Override
    public void handleLoginLog(SysLoginLogQo qo, ResultHandler<SysLoginLogVO> handler) {
        this.baseMapper.handleLoginLog(qo,handler);
    }

    @Override
    public Boolean insertLoginLog(LoginLogDTO dto) {
        SysLoginLogDO logDO = ConvertUtil.sourceToTarget(dto, SysLoginLogDO.class);
        baseMapper.insert(logDO);
        return true;
    }

}
