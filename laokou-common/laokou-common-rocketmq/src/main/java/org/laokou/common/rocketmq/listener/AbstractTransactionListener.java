/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.apis.producer.TransactionResolution;
import org.apache.rocketmq.client.core.RocketMQTransactionChecker;
import org.apache.rocketmq.client.support.RocketMQHeaders;
import org.laokou.common.i18n.utils.ObjectUtil;

/**
 * @author laokou
 */
@Slf4j
public abstract class AbstractTransactionListener implements RocketMQTransactionChecker {

	@Override
	public TransactionResolution check(MessageView messageView) {
		String transactionId = ObjectUtil
			.requireNotNull(messageView.getProperties().get(RocketMQHeaders.TRANSACTION_ID));
		log.info("事务回查");
		if (checkLocalTransaction(transactionId)) {
			log.info("事务回查后，提交");
			return TransactionResolution.COMMIT;
		}
		else {
			log.info("事务回查后，回滚");
			return TransactionResolution.ROLLBACK;
		}
	}

	/**
	 * 本地事务检查.
	 * @param transactionId transactionId
	 * @return boolean
	 */
	protected abstract boolean checkLocalTransaction(String transactionId);

}
