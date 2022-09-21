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
package io.laokou.admin.application.service.impl;
import io.laokou.admin.application.service.SysMonitorApplicationService;
import io.laokou.admin.interfaces.vo.CacheVO;
import io.laokou.admin.interfaces.vo.ServerVO;
import io.laokou.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/27 0027 下午 3:18
 */
@Service
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
public class SysMonitorApplicationServiceImpl implements SysMonitorApplicationService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public CacheVO getCacheInfo() {
        CacheVO vo = new CacheVO();
        vo.setCommandStats(redisUtil.getCommandStatus());
        vo.setInfo(redisUtil.getInfo());
        vo.setKeysSize(redisUtil.getKeysSize());
        return vo;
    }

    @Override
    public ServerVO getServerInfo() throws Exception {
        ServerVO vo = new ServerVO();
        vo.copyTo();
        return vo;
    }
}
