/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.elasticsearch;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.laokou.common.elasticsearch.annotation.Field;
import org.laokou.common.elasticsearch.annotation.Index;
import org.laokou.common.elasticsearch.annotation.Type;

import java.io.Serializable;

@Data
@Index
class TestProject implements Serializable {

	@JsonSerialize(using = ToStringSerializer.class)
	@Field(type = Type.LONG)
	private Long businessKey;

}
