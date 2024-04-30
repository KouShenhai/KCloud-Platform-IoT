/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.common.i18n.common;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author laokou
 */
@Schema(name = "ElasticsearchIndexConstants", description = "Elasticsearch索引常量")
public final class ElasticsearchIndexConstant {

	private ElasticsearchIndexConstant() {
	}

	@Schema(name = "RESOURCE", description = "资源索引")
	public static final String RESOURCE = "laokou_resource";

	@Schema(name = "TRACE", description = "分布式链路索引")
	public static final String TRACE = "laokou_trace";

}
