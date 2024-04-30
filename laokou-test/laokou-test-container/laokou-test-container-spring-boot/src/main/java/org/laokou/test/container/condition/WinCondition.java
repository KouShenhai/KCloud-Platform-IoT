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

package org.laokou.test.container.condition;

import io.micrometer.common.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.laokou.test.container.annotation.ConditionalOnWin;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

/**
 * @author laokou
 */
@Slf4j
@NonNullApi
public class WinCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(ConditionalOnWin.class.getName(),
				true);
		log.info("evn：{}", context.getEnvironment().getDefaultProfiles()[0]);
		return attributes != null && !attributes.isEmpty() && "win".equals(attributes.getFirst("env"));
	}

}
