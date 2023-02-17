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
    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8
            , 16
            , 60
            , TimeUnit.SECONDS
            , new LinkedBlockingQueue(256)
            , ThreadUtil.createThreadFactory("laokou-common-mybatis-plus-thread")
            , new ThreadPoolExecutor.CallerRunsPolicy());
    /**
     * 多线程批量新增
     * @param dataList 集合
     * @param batchNum 每组多少条数据
     * @param service 基础service
     */
//    @SneakyThrows
//    public void insertConcurrentBatch(List<T> dataList, int batchNum, BatchService<T> service) {
//        // 数据分组
//        List<List<T>> partition = Lists.partition(dataList, batchNum);
//        int size = partition.size();
//        CountDownLatch latch = new CountDownLatch(size);
//        // 标识事务状态
//        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
//        // 存放事务状态
//        List<TransactionStatus> transactionStatus = Collections.synchronizedList(new ArrayList<>(size));
//        for(int i = 0; i < size; i++) {
//            List<T> list = partition.get(i);
//            threadPoolExecutor.execute(() -> {
//                try {
//                    TransactionStatus status = transactionalUtil.begin();
//                    transactionStatus.add(status);
//                    service.insertBatch(list);
//                } catch (Exception e) {
//                    // 标识为true，回滚事务
//                    atomicBoolean.set(true);
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//        boolean timeout = latch.await(30, TimeUnit.SECONDS);
//        if (timeout) {
//            atomicBoolean.set(true);
//        }
//        if (CollectionUtils.isNotEmpty(transactionStatus)) {
//            if (atomicBoolean.get()) {
//                // 回滚事务
//                transactionStatus.forEach(status -> transactionalUtil.rollback(status));
//                throw new RuntimeException("数据无法插入，请联系管理员");
//            } else {
//                transactionStatus.forEach(status -> transactionalUtil.commit(status));
//            }
//        }
//    }

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
            threadPoolExecutor.execute(() -> {
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
