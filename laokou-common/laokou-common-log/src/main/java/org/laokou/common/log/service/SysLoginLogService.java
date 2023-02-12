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
import org.apache.ibatis.session.ResultHandler;
import org.laokou.common.log.dto.LoginLogDTO;
import org.laokou.common.log.qo.SysLoginLogQo;
import org.laokou.common.log.vo.SysLoginLogVO;
/**
 * @author laokou
 */
public interface SysLoginLogService {
    /**
     * 分页查询登录日志
     * @param page
     * @param qo
     * @return
     */
    IPage<SysLoginLogVO> getLoginLogList(IPage<SysLoginLogVO> page, SysLoginLogQo qo);

    /**
     * 查询登录日志
     * @param qo
     * @param handler
     * @return
     */
    void handleLoginLog(SysLoginLogQo qo, ResultHandler<SysLoginLogVO> handler);

    /**
     * 新增登录日志
     * @param dto
     * @return
     */
    Boolean insertLoginLog(LoginLogDTO dto);

}
