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

package org.laokou.common.extension;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * because {@link Extension} only supports single coordinates, this annotation is a
 * supplement to {@link Extension} and supports multiple coordinates.
 *
 * @author wangguoqiang wrote on 2022/10/10 12:19
 * @version 1.0
 * @see Extension
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Component
public @interface Extensions {

	String[] bizId() default BizScenario.DEFAULT_BIZ_ID;

	String[] useCase() default BizScenario.DEFAULT_USE_CASE;

	String[] scenario() default BizScenario.DEFAULT_SCENARIO;

	Extension[] value() default {};

}
