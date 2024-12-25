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

package org.laokou.common.rocketmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.laokou.common.core.utils.MDCUtil;
import org.laokou.common.i18n.utils.ObjectUtil;import org.springframework.messaging.Message;

import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;
import static org.laokou.common.i18n.common.constant.TraceConstant.SPAN_ID;
import static org.laokou.common.i18n.common.constant.TraceConstant.TRACE_ID;

/**
 * @author laokou
 */
@Slf4j
public abstract class AbstractTransactionConsumer implements RocketMQLocalTransactionListener {

	@Override
	public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object args) {
		try {
			putTrace(message);
			executeExtLocalTransaction(message, args);
			log.info("执行本地事务，事务提交");
			return RocketMQLocalTransactionState.COMMIT;
		}
		catch (Exception e) {
			log.error("执行本地事务，事务回滚，错误信息：{}", e.getMessage(), e);
			return RocketMQLocalTransactionState.ROLLBACK;
		}
		finally {
			clearTrace();
		}
	}

	@Override
	public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
		try {
			putTrace(message);
			if (checkExtLocalTransaction(message)) {
				log.info("事务回查后，事务提交");
				return RocketMQLocalTransactionState.COMMIT;
			}
			log.info("事务回查后，事务回滚");
			return RocketMQLocalTransactionState.ROLLBACK;
		}
		catch (Exception e) {
			log.error("事务回查异常，事务回滚，错误信息：{}", e.getMessage(), e);
			return RocketMQLocalTransactionState.ROLLBACK;
		}
		finally {
			clearTrace();
		}
	}

	/**
	 * 本地事务实现.
	 * @param message 消息
	 * @param args 参数
	 */
	protected abstract void executeExtLocalTransaction(Message message, Object args);

	/**
	 * 本地事务检查.
	 * @param message 消息
	 * @return boolean
	 */
	protected abstract boolean checkExtLocalTransaction(Message message);

	private void putTrace(Message message) {
		Object obj1 = message.getHeaders().get(TRACE_ID);
		Object obj2 = message.getHeaders().get(SPAN_ID);
		String traceId = ObjectUtil.isNull(obj1) ? EMPTY : obj1.toString();
		String spanId = ObjectUtil.isNull(obj2) ? EMPTY : obj2.toString();
		MDCUtil.put(traceId, spanId);
	}

	private void clearTrace() {
		MDCUtil.clear();
	}

}
