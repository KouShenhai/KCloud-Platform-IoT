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
import org.laokou.common.mybatisplus.service.BatchService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.util.List;
import java.util.concurrent.*;
/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BatchUtil<T> {

    private final TransactionalUtil transactionalUtil;

    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(9
            , 9
            , 60
            , TimeUnit.SECONDS
            , new LinkedBlockingQueue<>(256)
            , ThreadUtil.createThreadFactory("laokou-common-mybatis-plus-thread-%d")
            // 线程池拒绝策略不是AbortPolicy，阻塞队列满了新的任务会被抛弃而且不抛出异常，
            // CountDownLatch永远不会等于0，countDownLatch.await()会一直阻塞
            // AbortPolicy：直接抛出RejectedExecutionException异常阻止系统正常运行
            , new ThreadPoolExecutor.AbortPolicy());

    /**
     * 多线程批量新增
     * @param dataList 集合
     * @param batchNum 每组多少条数据
     * @param service 基础service
     */
    @SneakyThrows
    public void insertConcurrentBatch(List<T> dataList, int batchNum, BatchService<T> service) {
        // 数据分组
        List<List<T>> partition = Lists.partition(dataList, batchNum);
        int size = partition.size();
        for(int i = 0; i < size; i++) {
            List<T> list = partition.get(i);
            THREAD_POOL_EXECUTOR.execute(() -> {
                TransactionStatus status = transactionalUtil.begin();
                try {
                    service.insertBatch(list);
                    transactionalUtil.commit(status);
                } catch (Exception e) {
                    transactionalUtil.rollback(status);
                    log.error("错误信息：批量插入数据异常，已执行回滚");
                }
            });
        }
    }

}
