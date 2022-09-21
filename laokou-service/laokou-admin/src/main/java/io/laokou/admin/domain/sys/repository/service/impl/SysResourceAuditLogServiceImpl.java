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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.sys.entity.SysResourceAuditLogDO;
import io.laokou.admin.domain.sys.repository.mapper.SysResourceAuditLogMapper;
import io.laokou.admin.domain.sys.repository.service.SysResourceAuditLogService;
import io.laokou.admin.interfaces.vo.SysResourceAuditLogVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/26 0026 下午 5:35
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysResourceAuditLogServiceImpl extends ServiceImpl<SysResourceAuditLogMapper, SysResourceAuditLogDO> implements SysResourceAuditLogService {
    @Override
    public List<SysResourceAuditLogVO> getAuditLogList(Long resourceId) {
        return this.baseMapper.getAuditLogList(resourceId);
    }
}
