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

package com.yousen.report.task;

import com.yousen.report.dto.BaseReportDTO;
import com.yousen.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TaskHandler {

    private final ReportService reportService;

    /**
     * 每天0点跑前一天数据
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void sensorReportTask() {
        BaseReportDTO dto = new BaseReportDTO();
        String dateTime = LocalDateTime.now().plusDays(-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        dto.setTenantId(20210002);
        dto.setDateTime(dateTime);
        reportService.sensorReport(dto);
    }

}
