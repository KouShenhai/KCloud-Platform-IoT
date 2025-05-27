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

package org.laokou.common.data.cache.handler;
import lombok.RequiredArgsConstructor;
import org.laokou.common.data.cache.handler.event.RemovedCacheEvent;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import java.util.List;

/**
 * @author laokou
 */
@RequiredArgsConstructor
public class RemoveCacheEventHandler {

	private final List<CacheManager> cacheManagers;

	protected void handleDomainEvent() {
		RemovedCacheEvent evt = null;
		cacheManagers.forEach(item -> {
			Cache cache = item.getCache(evt.name());
			if (ObjectUtils.isNotNull(cache)) {
				String key = evt.key();
				if (StringUtils.isNotEmpty(key)) {
					cache.evictIfPresent(key);
				}
				else {
					cache.clear();
				}
			}
		});
	}

}
