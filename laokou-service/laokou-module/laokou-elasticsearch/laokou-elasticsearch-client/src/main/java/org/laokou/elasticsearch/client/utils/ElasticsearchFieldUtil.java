/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.elasticsearch.client.utils;
import org.laokou.elasticsearch.client.index.ResourceIndex;

import java.util.HashMap;
import java.util.Map;
/**
 * @author laokou
 */
public class ElasticsearchFieldUtil {

    public static final String RESOURCE_INDEX = "laokou_resource";

    private static final Map<String,Class<?>> CLASS_MAP = new HashMap<>(16);

    static {
        CLASS_MAP.put(RESOURCE_INDEX, ResourceIndex.class);
    }

    public static Class<?> getClazz(final String indexName) {
        return CLASS_MAP.get(indexName);
    }
}
