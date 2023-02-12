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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.session.ResultHandler;
import org.laokou.common.log.dto.OperateLogDTO;
import org.laokou.common.log.entity.SysOperateLogDO;
import org.laokou.common.log.qo.SysOperateLogQo;
import org.laokou.common.log.vo.SysOperateLogVO;
/**
 * @author laokou
 */
public interface SysOperateLogService extends IService<SysOperateLogDO> {

    /**
     * 分页查询操作日志
     * @param page
     * @param qo
     * @return
     */
    IPage<SysOperateLogVO> getOperateLogList(IPage<SysOperateLogVO> page, SysOperateLogQo qo);

    /**
     * 查询操作日志
     * @param qo
     * @param handler
     */
    void handleLoginLog(SysOperateLogQo qo, ResultHandler<SysOperateLogVO> handler);

    /**
     * 新增登录日志
     * @param dto
     * @return
     */
    Boolean insertOperateLog(OperateLogDTO dto);

}
