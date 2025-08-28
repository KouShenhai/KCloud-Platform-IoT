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

/*
 * Copyright 2020-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.common.security.config.convertor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.laokou.common.fory.config.ForyFactory;
import org.laokou.common.security.config.entity.OAuth2AuthorizationGrantAuthorization;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
/**
 * @author spring-authorization-server
 * @author laokou
 */
@NoArgsConstructor
@ReadingConverter
public final class BytesToClaimsHolderConverter implements Converter<byte[], OAuth2AuthorizationGrantAuthorization.ClaimsHolder> {

	@Override
	public OAuth2AuthorizationGrantAuthorization.ClaimsHolder convert(@NotNull byte[] value) {
		return (OAuth2AuthorizationGrantAuthorization.ClaimsHolder) ForyFactory.INSTANCE.deserialize(value);
	}

}
