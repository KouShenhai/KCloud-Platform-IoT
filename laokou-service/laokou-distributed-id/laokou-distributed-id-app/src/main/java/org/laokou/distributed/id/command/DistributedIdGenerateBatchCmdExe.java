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

package org.laokou.distributed.id.command;

import lombok.RequiredArgsConstructor;
import org.laokou.common.core.util.ThreadUtils;
import org.laokou.common.i18n.dto.Result;
import org.laokou.distributed.identifier.config.SnowflakeGenerator;
import org.laokou.distributed.identifier.dto.DistributedIdGenerateBatchCmd;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DistributedIdGenerateBatchCmdExe {

	private final SnowflakeGenerator snowflakeGenerator;

	public Result<List<Long>> execute(DistributedIdGenerateBatchCmd cmd) {
		try (ExecutorService virtualTaskExecutor = ThreadUtils.newVirtualTaskExecutor()) {
			Integer num = cmd.num();
			List<Callable<Long>> futures = new ArrayList<>(num);
			List<Long> list = new ArrayList<>(num);
			for (int i = 0; i < num; i++) {
				futures.add(snowflakeGenerator::nextId);
			}
			virtualTaskExecutor.invokeAll(futures);
			for (Callable<Long> future : futures) {
				list.add(future.call());
			}
			// 排序
			Collections.sort(list);
			return Result.ok(list);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
