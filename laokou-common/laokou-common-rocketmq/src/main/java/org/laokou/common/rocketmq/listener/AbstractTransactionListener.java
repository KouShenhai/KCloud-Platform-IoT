/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.common.rocketmq.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.messaging.Message;

/**
 * @author laokou
 */
@Slf4j
public abstract class AbstractTransactionListener implements RocketMQLocalTransactionListener {

	@Override
	public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object args) {
		String transactionId = ObjectUtil.requireNotNull(message.getHeaders().get(RocketMQHeaders.TRANSACTION_ID))
			.toString();
		Object payload = message.getPayload();
		log.info("执行本地事务");
		try {
			executeLocalTransaction(payload, transactionId);
			log.info("事务提交");
			return RocketMQLocalTransactionState.COMMIT;
		}
		catch (Exception e) {
			log.error("事务回滚", e);
			return RocketMQLocalTransactionState.ROLLBACK;
		}
	}

	@Override
	public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
		String transactionId = ObjectUtil.requireNotNull(message.getHeaders().get(RocketMQHeaders.TRANSACTION_ID))
			.toString();
		log.info("事务回查");
		if (checkLocalTransaction(transactionId)) {
			log.info("事务回查后，提交");
			return RocketMQLocalTransactionState.COMMIT;
		}
		else {
			log.info("事务回查后，回滚");
			return RocketMQLocalTransactionState.ROLLBACK;
		}
	}

	/**
	 * 本地事务实现.
	 * @param transactionId transactionId
	 * @param obj obj
	 */
	protected abstract void executeLocalTransaction(Object obj, String transactionId);

	/**
	 * 本地事务检查.
	 * @param transactionId transactionId
	 * @return boolean
	 */
	protected abstract boolean checkLocalTransaction(String transactionId);

}
