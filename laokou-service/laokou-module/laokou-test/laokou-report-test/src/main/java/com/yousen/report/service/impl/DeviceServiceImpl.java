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
package com.yousen.report.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yousen.report.entity.DeviceDO;
import com.yousen.report.entity.SensorDayTimeRT;
import com.yousen.report.service.DeviceService;
import lombok.RequiredArgsConstructor;
import com.yousen.report.mapper.DeviceMapper;
import com.yousen.report.vo.SensorStatisticVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, DeviceDO> implements DeviceService {

    public List<String> getDeviceIds(Integer tenantId) {
        return this.baseMapper.getDeviceIds(tenantId);
    }

    @Override
    public List<SensorStatisticVO> getSensorStatisticList(List<String> list, String dateTime, Integer maxWight, Integer minWight) {
        return this.baseMapper.getSensorStatisticList(list,dateTime,maxWight,minWight);
    }

    @Override
    public List<SensorDayTimeRT> getSensorDayTimeRTList(List<String> list, String dateTime) {
        return this.baseMapper.getSensorDayTimeRTList(list,dateTime);
    }

    @Override
    public List<String> getSensorStatisticTables(List<String> list) {
        return this.baseMapper.getSensorStatisticTables(list);
    }


}
