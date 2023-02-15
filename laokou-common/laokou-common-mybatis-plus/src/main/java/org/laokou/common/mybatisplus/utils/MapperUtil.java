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
package org.laokou.common.mybatisplus.utils;
import cn.hutool.core.thread.ThreadUtil;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.exception.CustomException;
import org.laokou.common.core.utils.StringUtil;
import org.laokou.common.mybatisplus.mapper.BaseBatchMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MapperUtil<T> {

    private final TransactionalUtil transactionalUtil;
    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8
            , 16
            , 60
            , TimeUnit.SECONDS
            , new LinkedBlockingQueue(256)
            , ThreadUtil.createThreadFactory("laokou-common-mybatis-plus-thread")
            , new ThreadPoolExecutor.CallerRunsPolicy());
    /**
     * 多数据源出现了事务导致超时异常，等待高手解决
     * 多线程批量新增
     * @param dataList 集合
     * @param batchNum 每组多少条数据
     * @param baseBatchMapper 基础mapper
     */
    @SneakyThrows
    public void insertConcurrentBatch(List<T> dataList, int batchNum, BaseBatchMapper<T> baseBatchMapper) {
        // 数据分组
        List<List<T>> partition = Lists.partition(dataList, batchNum);
        int size = partition.size();
        CountDownLatch latch = new CountDownLatch(size);
        // 标识事务状态
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        // 存放事务状态
        List<TransactionStatus> transactionStatus = new ArrayList<>(size);
        for(int i = 0; i < size; i++) {
            List<T> list = partition.get(i);
            threadPoolExecutor.execute(() -> {
                try {
                    DefaultTransactionAttribute defaultTransactionAttribute = new DefaultTransactionAttribute();
                    // 隔离级别设为创建新事务
                    defaultTransactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                    TransactionStatus status = transactionalUtil.begin(defaultTransactionAttribute);
                    transactionStatus.add(status);
                    baseBatchMapper.insertBatch(list);
                } catch (Exception e) {
                    // 标识为true，回滚事务
                    atomicBoolean.set(true);
                    String message = e.getMessage();
                    log.error("错误信息：{}", StringUtil.isEmpty(message) ? "sql执行错误" : message);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        if (atomicBoolean.get()) {
            // 回滚事务
            transactionStatus.forEach(status -> transactionalUtil.rollback(status));
            throw new CustomException("数据无法插入，请联系管理员");
        } else {
            transactionStatus.forEach(status -> transactionalUtil.commit(status));
        }
    }

    /**
     * 批量新增
     * @param dataList 集合
     * @param batchNum 每组多少条数据
     * @param baseBatchMapper 基础mapper
     */
    public void insertBatch(List<T> dataList, int batchNum, BaseBatchMapper<T> baseBatchMapper) {
        // 数据分组
        List<List<T>> partition = Lists.partition(dataList, batchNum);
        int size = partition.size();
        for (int i = 0; i < size; i++) {
            List<T> list = partition.get(i);
            TransactionStatus status = transactionalUtil.begin();
            try {
                baseBatchMapper.insertBatch(list);
                transactionalUtil.commit(status);
            } catch (Exception e) {
                transactionalUtil.rollback(status);
                String message = e.getMessage();
                log.error("错误信息：{}", StringUtil.isEmpty(message) ? "sql执行错误" : message);
                throw new CustomException("数据无法插入，请联系管理员");
            }
        }
    }

}
