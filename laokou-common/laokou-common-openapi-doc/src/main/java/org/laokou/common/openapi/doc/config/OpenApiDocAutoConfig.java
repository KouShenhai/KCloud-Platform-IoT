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

package org.laokou.common.openapi.doc.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import static org.laokou.common.i18n.common.constants.Constant.AUTHORIZATION;

/**
 * @author laokou
 */
@AutoConfiguration
public class OpenApiDocAutoConfig {

	@Bean
	@ConditionalOnMissingBean(OpenAPI.class)
	OpenAPI openApi() {
		return new OpenAPI()
			.info(new Info().title("API文档")
				.description("API文档")
				.version("3.3.0")
				.contact(new Contact().name("laokou").url("https://github.com/KouShenhai").email("2413176044@qq.com"))
				.license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html")))
			.externalDocs(new ExternalDocumentation().description("老寇IoT云平台").url("https://github.com/KouShenhai"))
			.addSecurityItem(new SecurityRequirement().addList(AUTHORIZATION))
			.components(new Components().addSecuritySchemes(AUTHORIZATION,
					new SecurityScheme().name(AUTHORIZATION)
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")));

	}

}
