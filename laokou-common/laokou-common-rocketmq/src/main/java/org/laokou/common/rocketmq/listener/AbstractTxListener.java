/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.common.rocketmq.listener;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Slf4j
@Component
@RocketMQTransactionListener
public abstract class AbstractTxListener implements RocketMQLocalTransactionListener {

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object args) {
        String transactionId = message.getHeaders().get(RocketMQHeaders.TRANSACTION_ID).toString();
        Object payload = message.getPayload();
        try {
            executeLocalTransaction(payload,transactionId);
            log.info("事务提交");
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            log.error("事务回滚：{}",e.getMessage());
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        String transactionId = message.getHeaders().get(RocketMQHeaders.TRANSACTION_ID).toString();
        log.info("事务回查");
        if (checkLocalTransaction(transactionId)) {
            log.info("事务回查后，提交");
            return RocketMQLocalTransactionState.COMMIT;
        } else {
            log.info("事务回查后，回滚");
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    /**
     * 本地事务实现
     * @param transactionId
     * @param obj
     * @return
     */
    abstract void executeLocalTransaction(Object obj,String transactionId);

    /**
     * 本地事务检查
     * @param transactionId
     * @return
     */
    abstract boolean checkLocalTransaction(String transactionId);

}
