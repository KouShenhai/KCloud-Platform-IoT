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

package org.laokou.common.mybatisplus.utils;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.laokou.common.core.utils.ThreadUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

import static com.baomidou.dynamic.datasource.enums.DdConstants.MASTER;

/**
 * @author why
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MybatisUtil {

	private static final int DEFAULT_BATCH_NUM = 100000;

	private static final ExecutorService EXECUTOR = ThreadUtil.newVirtualTaskExecutor();

	private final SqlSessionFactory sqlSessionFactory;

	public <T, M> void batch(List<T> dataList, int batchNum, int timeout, Class<M> clazz, BiConsumer<M, T> consumer) {
		batch(dataList, batchNum, timeout, clazz, MASTER, consumer);
	}

	public <T, M> void batch(List<T> dataList, Class<M> clazz, BiConsumer<M, T> consumer) {
		batch(dataList, DEFAULT_BATCH_NUM, 180, clazz, MASTER, consumer);
	}

	public <T, M> void batch(List<T> dataList, Class<M> clazz, String ds, BiConsumer<M, T> consumer) {
		batch(dataList, DEFAULT_BATCH_NUM, 180, clazz, ds, consumer);
	}

	/**
	 * 批量新增.
	 * @param dataList 集合
	 * @param batchNum 每组多少条数据
	 * @param clazz 类型
	 * @param <T> 泛型
	 * @param <M> mapper泛型
	 * @param consumer 函数
	 * @param ds 数据源名称
	 */
	public <T, M> void batch(List<T> dataList, int batchNum, int timeout, Class<M> clazz, String ds,
			BiConsumer<M, T> consumer) {
		try {
			// 数据分组
			List<List<T>> partition = Lists.partition(dataList, batchNum);
			AtomicBoolean rollback = new AtomicBoolean(false);
			CyclicBarrier cyclicBarrier = new CyclicBarrier(partition.size());
			// 虚拟线程
			List<Callable<Boolean>> futures = partition.stream().map(item -> (Callable<Boolean>) () -> {
				handleBatch(timeout, item, clazz, consumer, rollback, ds, cyclicBarrier);
				return true;
			}).toList();
			// 执行任务
			EXECUTOR.invokeAll(futures);
			if (rollback.get()) {
				throw new SystemException("S_DS_TransactionRolledBack", "事务已回滚");
			}
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("错误信息：{}", e.getMessage());
			throw new SystemException("S_UnKnow_Error", e.getMessage(), e);
		}
	}

	@SneakyThrows
	private <T, M> void handleBatch(int timeout, List<T> item, Class<M> clazz, BiConsumer<M, T> consumer,
			AtomicBoolean rollback, String ds, CyclicBarrier cyclicBarrier) {
		try {
			DynamicDataSourceContextHolder.push(ds);
			SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
			M mapper = sqlSession.getMapper(clazz);
			// commit 执行 flushStatements()
			// rollback 执行 flushStatements(true);
			try {
				item.forEach(i -> consumer.accept(mapper, i));
				// 阻塞线程【默认180秒】
				cyclicBarrier.await(timeout, TimeUnit.SECONDS);
				// 默认SQL执行没有问题
				sqlSession.commit();
			}
			catch (Exception e) {
				handleException(rollback, e, cyclicBarrier);
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

	private void handleException(AtomicBoolean rollback, Exception e, CyclicBarrier cyclicBarrier) {
		// 回滚标识
		rollback.compareAndSet(false, true);
		log.error("批量插入数据异常，已设置回滚标识，错误信息", e);
		cyclicBarrier.reset();
	}

}
