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

package org.laokou.common.mybatisplus.utils;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.mybatisplus.mapper.CrudMapper;
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

	private static final int DEFAULT_PARTITION_SIZE = 100000;

	private static final int DEFAULT_BATCH_SIZE = 10000;

	private final SqlSessionFactory sqlSessionFactory;

	private final ExecutorService virtualThreadExecutor;

	public <DO, MAPPER extends CrudMapper<?, ?, DO>> void batch(List<DO> dataList, int partitionSize, int batchSize,
			int timeout, Class<MAPPER> clazz, BiConsumer<MAPPER, DO> consumer) {
		batch(dataList, partitionSize, batchSize, timeout, clazz, MASTER, consumer);
	}

	public <DO, MAPPER extends CrudMapper<?, ?, DO>> void batch(List<DO> dataList, Class<MAPPER> clazz,
			BiConsumer<MAPPER, DO> consumer) {
		batch(dataList, DEFAULT_PARTITION_SIZE, DEFAULT_BATCH_SIZE, 180, clazz, MASTER, consumer);
	}

	public <DO, MAPPER extends CrudMapper<?, ?, DO>> void batch(List<DO> dataList, Class<MAPPER> clazz, String ds,
			BiConsumer<MAPPER, DO> consumer) {
		batch(dataList, DEFAULT_PARTITION_SIZE, DEFAULT_BATCH_SIZE, 180, clazz, ds, consumer);
	}

	/**
	 * 批量新增.
	 * @param dataList 集合
	 * @param partitionSize 分组大小
	 * @param clazz 类型
	 * @param <DO> 泛型
	 * @param <MAPPER> mapper泛型
	 * @param consumer 函数
	 * @param ds 数据源名称
	 * @param timeout 超时时间
	 * @param batchSize 批次大小
	 */
	public <DO, MAPPER extends CrudMapper<?, ?, DO>> void batch(List<DO> dataList, int partitionSize, int batchSize,
			int timeout, Class<MAPPER> clazz, String ds, BiConsumer<MAPPER, DO> consumer) {
		if (CollectionUtil.isNotEmpty(dataList)) {
			// 数据分组
			List<List<DO>> partition = Lists.partition(dataList, partitionSize);
			AtomicBoolean rollback = new AtomicBoolean(false);
			CyclicBarrier cyclicBarrier = new CyclicBarrier(partition.size());
			try {
				// 虚拟线程
				List<Callable<Boolean>> futures = partition.stream().map(item -> (Callable<Boolean>) () -> {
					handleBatch(timeout, batchSize, item, clazz, consumer, rollback, ds, cyclicBarrier);
					return true;
				}).toList();
				// 执行任务
				virtualThreadExecutor.invokeAll(futures);
				if (rollback.get()) {
					throw new SystemException("S_DS_TransactionRolledBack", "事务已回滚");
				}
			}
			catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				log.error("SQL执行失败，错误信息：{}", e.getMessage(), e);
				throw new SystemException("S_DS_UnKnowError", e.getMessage(), e);
			}
		}
	}

	private <DO, MAPPER extends CrudMapper<?, ?, DO>> void handleBatch(int timeout, int batchSize, List<DO> item,
			Class<MAPPER> clazz, BiConsumer<MAPPER, DO> consumer, AtomicBoolean rollback, String ds,
			CyclicBarrier cyclicBarrier) {
		try {
			DynamicDataSourceContextHolder.push(ds);
			SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
			MAPPER mapper = sqlSession.getMapper(clazz);
			try {
				int size = item.size();
				for (int i = 0; i < size; i++) {
					consumer.accept(mapper, item.get(i));
					if (i % batchSize == 0 || i == size - 1) {
						// 刷入批处理
						sqlSession.flushStatements();
						// 清理缓存防止 OOM
						sqlSession.clearCache();
					}
				}
				// 阻塞线程【默认180秒】
				cyclicBarrier.await(timeout, TimeUnit.SECONDS);
				// 提交事务
				sqlSession.commit();
			}
			catch (Exception e) {
				handleException(rollback, e, cyclicBarrier);
			}
			finally {
				if (rollback.get()) {
					sqlSession.rollback();
				}
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
