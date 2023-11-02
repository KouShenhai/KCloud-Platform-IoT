/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.laokou.common.i18n.common.exception.DataSourceException;
import org.laokou.common.mybatisplus.database.BatchMapper;
import org.laokou.common.mybatisplus.database.dataobject.BaseDO;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.baomidou.dynamic.datasource.enums.DdConstants.MASTER;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BatchUtil {

	private final ThreadPoolTaskExecutor taskExecutor;

	private final SqlSessionFactory sqlSessionFactory;

	private static final int DEFAULT_BATCH_NUM = 1000;

	public <T extends BaseDO, M extends BatchMapper<T>> void insertBatch(List<T> dataList, Class<M> clazz) {
		insertBatch(dataList, DEFAULT_BATCH_NUM, clazz, MASTER);
	}

	public <T extends BaseDO, M extends BatchMapper<T>> void insertBatch(List<T> dataList, Class<M> clazz, String ds) {
		insertBatch(dataList, DEFAULT_BATCH_NUM, clazz, ds);
	}

	/**
	 * 批量新增
	 * @param dataList 集合
	 * @param batchNum 每组多少条数据
	 * @param clazz 类型
	 */
	@SneakyThrows
	public <T extends BaseDO, M extends BatchMapper<T>> void insertBatch(List<T> dataList, int batchNum, Class<M> clazz,
			String ds) {
		// 数据分组
		List<List<T>> partition = Lists.partition(dataList, batchNum);
		AtomicBoolean rollback = new AtomicBoolean(false);
		CyclicBarrier cyclicBarrier = new CyclicBarrier(partition.size());
		List<CompletableFuture<Void>> futures = partition.parallelStream()
			.map(item -> CompletableFuture.runAsync(() -> handleBatch(item, clazz, cyclicBarrier, rollback, ds),
					taskExecutor))
			.toList();
		// 阻塞主线程
		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
		if (rollback.get()) {
			throw new DataSourceException("批量插入数据异常，数据已回滚");
		}
	}

	@SneakyThrows
	private <T extends BaseDO, M extends BatchMapper<T>> void handleBatch(List<T> item, Class<M> clazz,
			CyclicBarrier cyclicBarrier, AtomicBoolean rollback, String ds) {
		try {
			DynamicDataSourceContextHolder.push(ds);
			SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
			M mapper = sqlSession.getMapper(clazz);
			try {
				item.parallelStream().forEachOrdered(mapper::save);
				cyclicBarrier.await(30, TimeUnit.SECONDS);
				sqlSession.commit();
			}
			catch (Exception e) {
				handleException(cyclicBarrier, rollback, e);
			}
			finally {
				if (rollback.get()) {
					sqlSession.rollback();
				}
				sqlSession.clearCache();
				sqlSession.close();
			}
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

	private void handleException(CyclicBarrier cyclicBarrier, AtomicBoolean rollback, Exception e) {
		// 回滚标识
		rollback.compareAndSet(false, true);
		log.error("批量插入数据异常，已设置回滚标识，错误信息", e);
		cyclicBarrier.reset();
	}

}
