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

package com.yousen.report.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author laokou
 */
@Data
@TableName("dw_iot_device_day_report")
public class DeviceDayReportDO {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private String sysOrgCode;
    private Integer tenantId;
    private String devName;
    private String devId;
    private String plate;
    private Date reportDate;
    private Integer devStatus;
    @TableField("sensor_1_status")
    private Integer sensor1Status;
    @TableField("sensor_2_status")
    private Integer sensor2Status;
    @TableField("sensor_3_status")
    private Integer sensor3Status;
    @TableField("sensor_4_status")
    private Integer sensor4Status;
    @TableField("sensor_5_status")
    private Integer sensor5Status;
    @TableField("sensor_6_status")
    private Integer sensor6Status;
    private String remark;

}
