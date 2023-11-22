/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.gatewayimpl.database.dataindex;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.laokou.common.elasticsearch.annotation.ElasticsearchField;
import org.laokou.common.i18n.dto.BaseIndex;

import java.io.Serial;

/**
 * @author laokou
 */
@Data
public class ResourceIndex extends BaseIndex {

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
	private String ym;

}
