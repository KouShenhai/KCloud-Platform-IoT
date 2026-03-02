/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.id.generator.segment;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 号段模型 — 表示一个从 Redis 申请到的连续 ID 区间. [minId, maxId]
 *
 * @author laokou
 */
@Getter
final class Segment {

	private final long minId;

	private final long maxId;

	private final int step;

	private final int ref;

	private final AtomicLong cursor;

	public Segment(long maxId, int step, double loadFactor) {
		this.maxId = maxId;
		this.step = step;
		this.minId = maxId - step + 1;
		this.ref = (int) (loadFactor * step);
		this.cursor = new AtomicLong(this.minId);
	}

	public Segment(long minId, long maxId, long curId, int step, double loadFactor) {
		this.step = step;
		this.minId = minId;
		this.maxId = maxId;
		this.ref = (int) (loadFactor * step);
		this.cursor = new AtomicLong(curId);
	}

	public long nextId() {
		long id = this.cursor.getAndIncrement();
		return id > this.maxId ? -1 : id;
	}

	public List<Long> nextIds(int num) {
		int size = Math.min(this.step, num);
		List<Long> ids = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			ids.add(this.cursor.getAndIncrement());
		}
		return ids;
	}

	public boolean isNext() {
		return this.cursor.get() >= this.ref + this.minId;
	}

}
