/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.common.elasticsearch.v7.clientobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.dto.ClientObject;

import java.util.Map;

/**
 * 配置.
 *
 * @author laokou
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettingsCO extends ClientObject {

	private Index index;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Index {

		private String uuid;

		private String creation_date;

		private String number_of_replicas;

		private String number_of_shards;

		private String provided_name;

		private String refresh_interval;

		private Version version;

		private Routing routing;

		private Analysis analysis;

	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Version {

		private String created;

	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Routing {

		private Allocation allocation;

	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Allocation {

		private Include include;

	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Include {

		private String _tier_preference;

	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Analysis {

		private Map<String, Object> analyzer;

		private Map<String, Object> filter;

	}

}
