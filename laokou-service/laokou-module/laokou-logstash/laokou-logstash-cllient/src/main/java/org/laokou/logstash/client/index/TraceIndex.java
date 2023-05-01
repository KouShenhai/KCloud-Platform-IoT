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
package org.laokou.logstash.client.index;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.laokou.common.elasticsearch.annotation.ElasticsearchField;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author laokou
 */
@Data
public class TraceIndex implements Serializable {

    @Serial
    private static final long serialVersionUID = -4314847178115273665L;
    @ElasticsearchField
    private String app;

    @ElasticsearchField
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
    private Date timestamp;

    @ElasticsearchField
    private String userId;

    @ElasticsearchField
    private String username;

    @ElasticsearchField
    private String tenantId;

    @ElasticsearchField
    private String traceId;

    @ElasticsearchField
    private String level;

    @ElasticsearchField
    private String pid;

    @ElasticsearchField
    private String thread;

    @ElasticsearchField
    private String logger;

    @ElasticsearchField
    private String msg;

}
