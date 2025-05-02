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

package org.laokou.common.mybatisplus.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.exception.SystemException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import java.util.function.Consumer;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionalUtils {

	private final TransactionTemplate transactionTemplate;

	// @formatter:off
	public void executeInTransaction(DatabaseExecutorVoid executor) {
		executeInTransaction(executor, TransactionDefinition.PROPAGATION_REQUIRED, TransactionDefinition.ISOLATION_READ_COMMITTED, false);
	}

	public void executeInTransaction(DatabaseExecutorVoid executor, int isolationLevel, boolean readOnly) {
		executeInTransaction(executor, TransactionDefinition.PROPAGATION_REQUIRED, isolationLevel, readOnly);
	}

	public void executeInNewTransaction(DatabaseExecutorVoid executor) {
		executeInTransaction(executor, TransactionDefinition.PROPAGATION_REQUIRES_NEW, TransactionDefinition.ISOLATION_READ_COMMITTED, false);
	}

	public void executeInNewTransaction(DatabaseExecutorVoid executor, int isolationLevel, boolean readOnly) {
		executeInTransaction(executor, TransactionDefinition.PROPAGATION_REQUIRES_NEW, isolationLevel, readOnly);
	}

	public <T> T executeResultInTransaction(DatabaseExecutor<T> executor) {
		return executeResultInTransaction(executor, TransactionDefinition.PROPAGATION_REQUIRED, TransactionDefinition.ISOLATION_READ_COMMITTED, false);
	}

	public <T> T executeResultInTransaction(DatabaseExecutor<T> executor, int isolationLevel, boolean readOnly) {
		return executeResultInTransaction(executor, TransactionDefinition.PROPAGATION_REQUIRED, isolationLevel, readOnly);
	}

	public <T> T executeResultInNewTransaction(DatabaseExecutor<T> executor) {
		return executeResultInTransaction(executor, TransactionDefinition.PROPAGATION_REQUIRES_NEW, TransactionDefinition.ISOLATION_READ_COMMITTED, false);
	}

	public <T> T executeResultInNewTransaction(DatabaseExecutor<T> executor, int isolationLevel, boolean readOnly) {
		return executeResultInTransaction(executor, TransactionDefinition.PROPAGATION_REQUIRES_NEW, isolationLevel, readOnly);
	}

	public void executeInTransaction(DatabaseExecutorVoid executor, int propagationBehavior, int isolationLevel, boolean readOnly) {
		executeWithoutResult(r -> {
			try {
				executor.execute();
			}
			catch (Exception e) {
				r.setRollbackOnly();
				log.error("操作失败，错误信息：{}", e.getMessage());
				throw new SystemException("S_DS_OperateError", e.getMessage(), e);
			}
		}, propagationBehavior,isolationLevel, readOnly);
	}

	public <T> T executeResultInTransaction(DatabaseExecutor<T> executor, int propagationBehavior, int isolationLevel, boolean readOnly) {
		return execute(r -> {
			try {
				return executor.execute();
			}
			catch (Exception e) {
				r.setRollbackOnly();
				log.error("操作失败，错误信息：{}", e.getMessage());
				throw new SystemException("S_DS_OperateError", e.getMessage(), e);
			}
		}, propagationBehavior,isolationLevel, readOnly);
	}

	private <T> T execute(TransactionCallback<T> action, int propagationBehavior, int isolationLevel, boolean readOnly) {
        return convert(propagationBehavior, isolationLevel, readOnly).execute(action);
	}

	private void executeWithoutResult(Consumer<TransactionStatus> action, int propagationBehavior, int isolationLevel, boolean readOnly) {
        convert(propagationBehavior, isolationLevel, readOnly).executeWithoutResult(action);
	}

	private TransactionTemplate convert(int propagationBehavior, int isolationLevel, boolean readOnly) {
        PlatformTransactionManager transactionManager = transactionTemplate.getTransactionManager();
        Assert.notNull(transactionManager, "TransactionManager must not be null");
        TransactionTemplate tranTemplate = new TransactionTemplate(transactionManager);
        tranTemplate.setPropagationBehavior(propagationBehavior);
        tranTemplate.setIsolationLevel(isolationLevel);
        tranTemplate.setReadOnly(readOnly);
		return tranTemplate;
	}

	@FunctionalInterface
	public interface DatabaseExecutorVoid {

		void execute()throws JsonProcessingException;

	}

	@FunctionalInterface
	public interface DatabaseExecutor<T> {

		T execute();

	}
    // @formatter:on

}
