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

package org.laokou.common.algorithm.template.select;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author laokou
 */
public class PollSelectAlgorithm<T> extends AbstractSelectAlgorithm<T> {

	private static final AtomicInteger ATOMIC = new AtomicInteger(-1);

	@Override
	public T select(List<T> list, Object arg) {
		if (ATOMIC.incrementAndGet() == list.size()) {
			ATOMIC.compareAndSet(ATOMIC.get(), 0);
		}
		return list.get(ATOMIC.get());
	}

}
