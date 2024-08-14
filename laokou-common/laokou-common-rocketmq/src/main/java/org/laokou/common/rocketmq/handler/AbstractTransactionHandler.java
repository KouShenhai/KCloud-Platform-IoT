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

package org.laokou.common.rocketmq.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.apis.producer.TransactionResolution;
import org.apache.rocketmq.client.core.RocketMQTransactionChecker;

/**
 * @author laokou
 */
@Slf4j
public abstract class AbstractTransactionHandler implements RocketMQTransactionChecker {

	@Override
	public TransactionResolution check(MessageView messageView) {
		return TransactionResolution.COMMIT;
	}

	//
//	@Override
//	public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object args) {
//		try {
//			executeExtLocalTransaction(message, args);
//			log.info("执行本地事务，事务提交");
//			return RocketMQLocalTransactionState.COMMIT;
//		}
//		catch (Exception e) {
//			log.error("执行本地事务，事务回滚，错误信息：{}", e.getMessage(), e);
//			return RocketMQLocalTransactionState.ROLLBACK;
//		}
//	}
//
//	@Override
//	public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
//		try {
//			if (checkExtLocalTransaction(message)) {
//				log.info("事务回查后，事务提交");
//				return RocketMQLocalTransactionState.COMMIT;
//			}
//			log.info("事务回查后，事务回滚");
//			return RocketMQLocalTransactionState.ROLLBACK;
//		}
//		catch (Exception e) {
//			log.error("事务回查异常，事务回滚，错误信息：{}", e.getMessage(), e);
//			return RocketMQLocalTransactionState.ROLLBACK;
//		}
//	}
//
//	/**
//	 * 本地事务实现.
//	 * @param message 消息
//	 * @param args 参数
//	 */
//	protected abstract void executeExtLocalTransaction(Message message, Object args);
//
//	/**
//	 * 本地事务检查.
//	 * @param message 消息
//	 * @return boolean
//	 */
//	protected abstract boolean checkExtLocalTransaction(Message message);

}
