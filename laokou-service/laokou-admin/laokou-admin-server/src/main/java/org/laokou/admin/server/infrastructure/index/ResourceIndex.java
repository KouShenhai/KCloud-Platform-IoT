/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.admin.server.infrastructure.index;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.laokou.common.elasticsearch.annotation.ElasticsearchField;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author laokou
 */
@Data
public class ResourceIndex implements Serializable {

	@Serial
	private static final long serialVersionUID = -3715061850731611381L;

	@ElasticsearchField(type = "long")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	@ElasticsearchField(type = "text", participle = 3)
	private String title;

	@ElasticsearchField
	private String code;

	@ElasticsearchField(type = "text", participle = 3)
	private String remark;

	@ElasticsearchField
	private String ymd;

}
