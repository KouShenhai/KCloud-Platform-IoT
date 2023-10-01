/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.common.elasticsearch.annotation;

import java.lang.annotation.*;

/**
 * Elasticsearch注解
 *
 * @author laokou
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ElasticsearchField {

	/**
	 * 默认 keyword
	 * @return
	 */
	String type() default "keyword";

	/**
	 * 0 not_analyzed 1 ik_smart 2.ik_max_word 3.ik-index(自定义分词器)
	 * @return
	 */
	int participle() default 0;

}
