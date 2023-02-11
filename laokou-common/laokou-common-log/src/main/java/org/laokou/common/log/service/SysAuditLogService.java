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
package org.laokou.common.log.service;
import java.util.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.laokou.common.log.dto.AuditLogDTO;
import org.laokou.common.log.entity.SysAuditLogDO;
import org.laokou.common.log.vo.SysAuditLogVO;

/**
 * @author laokou
 */
public interface SysAuditLogService extends IService<SysAuditLogDO> {
    /**
     * 分页查询审核日志
     * @param businessId
     * @param type
     * @return
     */
    List<SysAuditLogVO> getAuditLogList(Long businessId, Integer type);

    /**
     * 新增审批日志
     * @param dto
     * @return
     */
    Boolean insertAuditLog(AuditLogDTO dto);
}
