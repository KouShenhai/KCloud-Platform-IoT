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

package com.yousen.report.vo;

import lombok.Data;
/**
 * @author laokou
 */
@Data
public class SensorStatisticVO {

    /**
     * 设备编号
     */
    private String devId;
    /**
     * 传感器编号
     */
    private Integer adId;
    /**
     * 最大传感器采集数值
     */
    private Integer maxRT;
    /**
     * 最小传感器采集数值
     */
    private Integer minRT;

    /**
     * 重量最大阈值
     */
    private Integer maxWeightCount;

    /**
     * 重量最小阈值
     */
    private Integer minWeightCount;

}
