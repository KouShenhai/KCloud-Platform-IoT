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

package org.laokou.common.testcontainers.util;

import org.testcontainers.utility.DockerImageName;

/**
 * @author laokou
 */
public final class DockerImageNames {

	private DockerImageNames() {
	}

	public static DockerImageName elasticsearch(String tag) {
		return DockerImageName.parse("registry.cn-shenzhen.aliyuncs.com/koushenhai/elasticsearch9")
			.asCompatibleSubstituteFor("docker.elastic.co/elasticsearch/elasticsearch")
			.withTag(tag);
	}

	public static DockerImageName elasticsearch() {
		return elasticsearch("9.1.3");
	}

	public static DockerImageName nacos(String tag) {
		return DockerImageName.parse("nacos/nacos-server")
			.withTag(tag);
	}

	public static DockerImageName nacos() {
		return elasticsearch("v3.0.3");
	}

	public static DockerImageName postgresql(String tag) {
		return DockerImageName.parse("postgres")
			.withTag(tag);
	}

	public static DockerImageName postgresql() {
		return elasticsearch("latest");
	}

}
