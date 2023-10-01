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

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.GlobalException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static com.baomidou.dynamic.datasource.enums.DdConstants.MASTER;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BatchUtil {

	private final TransactionalUtil transactionalUtil;

	private final ThreadPoolTaskExecutor taskExecutor;

	private static final int DEFAULT_BATCH_NUM = 1000;

	public <T> void insertBatch(List<T> dataList, Consumer<List<T>> batchOps) {
		insertBatch(dataList, DEFAULT_BATCH_NUM, batchOps, MASTER);
	}

	public <T> void insertBatch(List<T> dataList, Consumer<List<T>> batchOps, String ds) {
		insertBatch(dataList, DEFAULT_BATCH_NUM, batchOps, ds);
	}

	/**
	 * 批量新增
	 * @param dataList 集合
	 * @param batchNum 每组多少条数据
	 * @param batchOps 函数
	 */
	@SneakyThrows
	public <T> void insertBatch(List<T> dataList, int batchNum, Consumer<List<T>> batchOps, String ds) {
		// 数据分组
		List<List<T>> partition = Lists.partition(dataList, batchNum);
		AtomicBoolean rollback = new AtomicBoolean(false);
		CyclicBarrier cyclicBarrier = new CyclicBarrier(partition.size());
		List<CompletableFuture<Void>> futures = partition.parallelStream()
			.map(item -> CompletableFuture.runAsync(() -> handleBatch(item, batchOps, cyclicBarrier, rollback, ds),
					taskExecutor))
			.toList();
		// 阻塞主线程
		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
		if (rollback.get()) {
			throw new GlobalException("批量插入数据异常，数据已回滚");
		}
	}

	private <T> void handleBatch(List<T> item, Consumer<List<T>> batchOps, CyclicBarrier cyclicBarrier,
			AtomicBoolean rollback, String ds) {
		try {
			DynamicDataSourceContextHolder.push(ds);
			transactionalUtil.executeWithoutResult(callback -> {
				try {
					batchOps.accept(item);
					cyclicBarrier.await(60, TimeUnit.SECONDS);
				}
				catch (Exception e) {
					handleException(cyclicBarrier, rollback, e.getMessage());
				}
				finally {
					if (rollback.get()) {
						callback.setRollbackOnly();
					}
				}
			});
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

	private void handleException(CyclicBarrier cyclicBarrier, AtomicBoolean rollback, String msg) {
		// 回滚标识
		rollback.compareAndSet(false, true);
		log.error("批量插入数据异常，已设置回滚标识，错误信息：{}", msg);
		cyclicBarrier.reset();
	}

}
