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

package org.laokou.common.elasticsearch.config;

import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties("spring.data.elasticsearch")
public class SpringElasticsearchProperties {

	private Set<String> uris = new HashSet<>(Collections.singletonList("http://localhost:9200"));

	private String username;

	private String password;

	private Duration connectionTimeout = Duration.ofSeconds(15L);

	private Duration socketTimeout = Duration.ofSeconds(30L);

	private boolean socketKeepAlive = false;

	private String pathPrefix;

	private String proxy;

	private String version = "9.2.0";

	private String clientVersion = "9.2.0";

	private final RestClient restClient = new RestClient();

	public record Node(String hostname, int port, ProtocolEnum protocol) {
	}

	public Set<Node> getNodes() {
		return this.getUris().stream().map(this::createNode).collect(Collectors.toSet());
	}

	private Node createNode(String uri) {
		if (!(uri.startsWith("http://") || uri.startsWith("https://"))) {
			uri = "http://" + uri;
		}
		return createNode(URI.create(uri));
	}

	private Node createNode(URI uri) {
		return new Node(uri.getHost(), uri.getPort(), ProtocolEnum.forScheme(uri.getScheme()));
	}

	@Data
	public static class RestClient {

		private final Sniffer sniffer = new Sniffer();

		private final Ssl ssl = new Ssl();

		@Data
		public static class Sniffer {

			/**
			 * Whether the sniffer is enabled.
			 */
			private boolean enabled = true;

			/**
			 * Interval between consecutive ordinary sniff executions.
			 */
			private Duration interval = Duration.ofMinutes(5);

			/**
			 * Delay of a sniff execution scheduled after a failure.
			 */
			private Duration delayAfterFailure = Duration.ofMinutes(1);

		}

		@Data
		public static class Ssl {

			/**
			 * SSL bundle name.
			 */
			private @Nullable String bundle;

		}

	}

}
