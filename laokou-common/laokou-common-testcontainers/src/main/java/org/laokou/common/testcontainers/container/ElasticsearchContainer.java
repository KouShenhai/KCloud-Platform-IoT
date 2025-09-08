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

package org.laokou.common.testcontainers.container;

import com.github.dockerjava.api.command.InspectContainerResponse;
import org.laokou.common.testcontainers.util.DockerImageNames;

import java.io.IOException;

/**
 * @author laokou
 */
public class ElasticsearchContainer extends org.testcontainers.elasticsearch.ElasticsearchContainer {

	private final String tag;

	public ElasticsearchContainer(String tag, String password) {
		super(DockerImageNames.elasticsearch(tag));
		super.withPassword(password);
		this.tag = tag;
	}

	@Override
	protected void containerIsStarted(InspectContainerResponse containerInfo) {
		try {
			super.execInContainer("elasticsearch-plugin", "install", "--batch", String.format("https://release.infinilabs.com/analysis-ik/stable/elasticsearch-analysis-ik-%s.zip", tag));
			super.execInContainer("elasticsearch-plugin", "install", "--batch", String.format("https://release.infinilabs.com/analysis-ik/stable/elasticsearch-analysis-ik-%s.zip", tag));
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
