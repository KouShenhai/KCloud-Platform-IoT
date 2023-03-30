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

package com.yousen.report.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yousen.report.entity.DeviceDayReportDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author laokou
 */
@Repository
@Mapper
public interface DeviceDayReportMapper extends BaseMapper<DeviceDayReportDO> {

    /**
     * 根据时间查询次数
     * @param dateTime
     * @return
     */
    Integer getDeviceDayReportCount(@Param("dateTime") String dateTime);
}
