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

package com.yousen.report.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yousen.report.entity.DeviceDO;
import com.yousen.report.entity.SensorDayTimeRT;
import com.yousen.report.vo.SensorStatisticVO;

import java.util.List;

/**
 * @author laokou
 */
public interface DeviceService extends IService<DeviceDO> {

    /**
     * 获取设备编号
     * @param tenantId
     * @return
     */
    List<String> getDeviceIds(Integer tenantId);

    /**
     * 获取设备传感器统计列表
     * @param list
     * @param dateTime
     * @param maxWight
     * @param minWight
     * @return
     */
    List<SensorStatisticVO> getSensorStatisticList(List<String> list, String dateTime, Integer maxWight, Integer minWight);

    /**
     * 获取传感器一天中第一次采集和最后一次采集
     * @param dateTime
     * @param list
     * @return
     */
    List<SensorDayTimeRT> getSensorDayTimeRTList(List<String> list, String dateTime);

    /**
     * 获取设备传感器表
     * @param list
     * @return
     */
    List<String> getSensorStatisticTables(List<String> list);

}
