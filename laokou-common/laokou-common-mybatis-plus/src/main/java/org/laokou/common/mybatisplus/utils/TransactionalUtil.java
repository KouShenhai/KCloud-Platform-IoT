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

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Consumer;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TransactionalUtil {

	private static final ThreadLocal<TransactionTemplate> TRANSACTION_LOCAL = new TransmittableThreadLocal<>();

	private final TransactionTemplate transactionTemplate;

	public void set() {
		TRANSACTION_LOCAL.set(transactionTemplate);
	}

	public void remove() {
		TRANSACTION_LOCAL.remove();
	}

	public <T> T execute(TransactionCallback<T> action, int propagationBehavior, int isolationLevel, boolean readOnly) {
		try {
			set();
			return convert(propagationBehavior, isolationLevel, readOnly).execute(action);
		}
		finally {
			remove();
		}
	}

	public <T> T defaultExecute(TransactionCallback<T> action, int isolationLevel, boolean readOnly) {
		return execute(action, TransactionDefinition.PROPAGATION_REQUIRED, isolationLevel, readOnly);
	}

	public <T> T defaultExecute(TransactionCallback<T> action) {
		return execute(action, TransactionDefinition.PROPAGATION_REQUIRED,
				TransactionDefinition.ISOLATION_READ_COMMITTED, false);
	}

	public <T> T newExecute(TransactionCallback<T> action, int isolationLevel, boolean readOnly) {
		return execute(action, TransactionDefinition.PROPAGATION_REQUIRES_NEW, isolationLevel, readOnly);
	}

	public <T> T newExecute(TransactionCallback<T> action) {
		return execute(action, TransactionDefinition.PROPAGATION_REQUIRES_NEW,
				TransactionDefinition.ISOLATION_READ_COMMITTED, false);
	}

	public void executeWithoutResult(Consumer<TransactionStatus> action, int propagationBehavior, int isolationLevel,
			boolean readOnly) {
		try {
			set();
			convert(propagationBehavior, isolationLevel, readOnly).executeWithoutResult(action);
		}
		finally {
			remove();
		}
	}

	public void defaultExecuteWithoutResult(Consumer<TransactionStatus> action, int isolationLevel, boolean readOnly) {
		executeWithoutResult(action, TransactionDefinition.PROPAGATION_REQUIRED, isolationLevel, readOnly);
	}

	public void defaultExecuteWithoutResult(Consumer<TransactionStatus> action) {
		executeWithoutResult(action, TransactionDefinition.PROPAGATION_REQUIRED,
				TransactionDefinition.ISOLATION_READ_COMMITTED, false);
	}

	public void newExecuteWithoutResult(Consumer<TransactionStatus> action) {
		executeWithoutResult(action, TransactionDefinition.PROPAGATION_REQUIRES_NEW,
				TransactionDefinition.ISOLATION_READ_COMMITTED, false);
	}

	public void newExecuteWithoutResult(Consumer<TransactionStatus> action, int isolationLevel, boolean readOnly) {
		executeWithoutResult(action, TransactionDefinition.PROPAGATION_REQUIRES_NEW, isolationLevel, readOnly);
	}

	private TransactionTemplate convert(int propagationBehavior, int isolationLevel, boolean readOnly) {
		TransactionTemplate transactionTemplate = TRANSACTION_LOCAL.get();
		transactionTemplate.setPropagationBehavior(propagationBehavior);
		transactionTemplate.setIsolationLevel(isolationLevel);
		transactionTemplate.setReadOnly(readOnly);
		return transactionTemplate;
	}

}
