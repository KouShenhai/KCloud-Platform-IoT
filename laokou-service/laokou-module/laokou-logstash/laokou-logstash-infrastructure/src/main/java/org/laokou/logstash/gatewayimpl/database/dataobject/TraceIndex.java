/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
 *
 */

package org.laokou.logstash.gatewayimpl.database.dataobject;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.laokou.common.elasticsearch.annotation.ElasticsearchField;
import org.laokou.common.i18n.dto.Index;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static org.laokou.common.i18n.utils.DateUtil.Constant.*;

/**
 * @author laokou
 */
@Data
public class TraceIndex extends Index {

    @ElasticsearchField
    private String appName;

    @ElasticsearchField
    private String profile;

    @ElasticsearchField
    @DateTimeFormat(pattern = YYYY_BAR_MM_BAR_DD_EMPTY_HH_RISK_HH_RISK_SS)
    @JsonFormat(pattern = YYYY_BAR_MM_BAR_DD_EMPTY_HH_RISK_HH_RISK_SS, timezone = DEFAULT_TIMEZONE)
    private LocalDateTime timestamp;

    @ElasticsearchField
    private String userId;

    @ElasticsearchField
    private String username;

    @ElasticsearchField
    private String tenantId;

    @ElasticsearchField
    private String traceId;

    @ElasticsearchField
    private String ip;

    @ElasticsearchField
    private String thread;

    @ElasticsearchField
    private String logger;

    @ElasticsearchField
    private String msg;

}
