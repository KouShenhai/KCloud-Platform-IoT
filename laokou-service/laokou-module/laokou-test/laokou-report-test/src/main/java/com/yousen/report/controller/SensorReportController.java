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

package com.yousen.report.controller;

import com.yousen.report.dto.BaseReportDTO;
import com.yousen.report.service.ReportService;
import com.yousen.report.utils.HttpResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author laokou
 */
@RestController
@RequestMapping("/sensor")
@RequiredArgsConstructor
public class SensorReportController {

    private final ReportService reportService;

    @PostMapping("/report")
    public HttpResult<Boolean> report(@RequestBody BaseReportDTO dto) {
        return new HttpResult<Boolean>().ok(reportService.sensorReport(dto));
    }

    @PostMapping("/init")
    public HttpResult<Boolean> init(@RequestBody BaseReportDTO dto) {
        return new HttpResult<Boolean>().ok(reportService.initYMReport(dto));
    }

}
