/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.influxdb.config;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.influx-db")
public class SpringInfluxDBProperties {

	public Type type = Type.TOKEN;

	@NotNull
	private String url;

	private String username;

	private String password;

	private String token;

	private String org;

	private String bucket;

	public enum Type {

		TOKEN, USERNAME_PASSWORD

	}

}
