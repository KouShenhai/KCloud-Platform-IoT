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
package org.laokou.common.mybatisplus.utils;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.core.CustomException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BatchUtil {

	private final TransactionalUtil transactionalUtil;

	private final ThreadPoolTaskExecutor taskExecutor;

	/**
	 * 批量新增
	 * @param dataList 集合
	 * @param batchNum 每组多少条数据
	 * @param service 基础service
	 */
	@SneakyThrows
	public <T> void insertBatch(List<T> dataList, int batchNum, Consumer<List<T>> batchOps) {
		// 数据分组
		List<List<T>> partition = Lists.partition(dataList, batchNum);
		AtomicBoolean rollback = new AtomicBoolean(false);
		List<CompletableFuture<Void>> futures = new ArrayList<>(partition.size());
		partition.forEach(item -> futures
				.add(CompletableFuture.runAsync(() -> transactionalUtil.executeWithoutResult(callback -> {
					try {
						batchOps.accept(item);
					}
					catch (Exception e) {
						// 回滚标识
						rollback.compareAndSet(false, true);
						log.error("批量插入数据异常，已设置回滚标识，错误信息：{}", e.getMessage());
					}
					finally {
						if (rollback.get()) {
							callback.setRollbackOnly();
						}
					}
				}), taskExecutor)));
		// 阻塞主线程
		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
		if (rollback.get()) {
			throw new CustomException("批量插入数据异常，数据已回滚");
		}
	}

}
