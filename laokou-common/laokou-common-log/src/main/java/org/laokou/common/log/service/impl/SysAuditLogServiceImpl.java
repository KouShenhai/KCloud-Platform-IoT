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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.log.dto.AuditLogDTO;
import org.laokou.common.log.entity.SysAuditLogDO;
import org.laokou.common.log.mapper.SysAuditLogMapper;
import org.laokou.common.log.service.SysAuditLogService;
import org.laokou.common.log.vo.SysAuditLogVO;
import org.springframework.stereotype.Service;
import java.util.*;
/**
 * @author laokou
 */
@Service
public class SysAuditLogServiceImpl extends ServiceImpl<SysAuditLogMapper, SysAuditLogDO> implements SysAuditLogService {

    @Override
    public List<SysAuditLogVO> getAuditLogList(Long businessId, Integer type) {
        return baseMapper.getAuditLogList(businessId,type);
    }

    @Override
    public Boolean insertAuditLog(AuditLogDTO dto) {
        SysAuditLogDO auditDO = ConvertUtil.sourceToTarget(dto, SysAuditLogDO.class);
        baseMapper.insert(auditDO);
        return true;
    }

}
