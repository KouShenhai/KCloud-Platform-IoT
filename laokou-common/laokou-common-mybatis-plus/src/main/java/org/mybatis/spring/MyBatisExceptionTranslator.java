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

/*
 * Copyright 2010-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mybatis.spring;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jetbrains.annotations.NotNull;
import org.laokou.common.core.util.SpringContextUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.transaction.TransactionException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.function.Supplier;

/**
 * Default exception translator.
 * <p>
 * Translates MyBatis SqlSession returned exception into a Spring
 * {@code DataAccessException} using Spring's {@code SQLExceptionTranslator} Can load
 * {@code SQLExceptionTranslator} eagerly or when the first exception is translated.
 *
 * @author Eduardo Macarron
 * @author laokou
 */
public class MyBatisExceptionTranslator implements PersistenceExceptionTranslator {

	private volatile SQLExceptionTranslator exceptionTranslator;

	private static final Object lock = new Object();

	/**
	 * Creates a new {@code PersistenceExceptionTranslator} instance with
	 * {@code SQLErrorCodeSQLExceptionTranslator}.
	 * @param dataSource DataSource to use to find metadata and establish which error
	 * codes are usable.
	 * @param exceptionTranslatorLazyInit if true, the translator instantiates internal
	 * stuff only the first time will have the need to translate exceptions.
	 */
	public MyBatisExceptionTranslator(DataSource dataSource, boolean exceptionTranslatorLazyInit) {
		this(() -> new SQLErrorCodeSQLExceptionTranslator(dataSource), exceptionTranslatorLazyInit);
	}

	/**
	 * Creates a new {@code PersistenceExceptionTranslator} instance with specified
	 * {@code SQLExceptionTranslator}.
	 * @param exceptionTranslatorSupplier Supplier for creating a
	 * {@code SQLExceptionTranslator} instance
	 * @param exceptionTranslatorLazyInit if true, the translator instantiates internal
	 * stuff only the first time will have the need to translate exceptions.
	 *
	 * @since 2.0.3
	 */
	public MyBatisExceptionTranslator(Supplier<SQLExceptionTranslator> exceptionTranslatorSupplier,
			boolean exceptionTranslatorLazyInit) {
		if (!exceptionTranslatorLazyInit) {
			this.initExceptionTranslator();
		}
	}

	@Override
	public DataAccessException translateExceptionIfPossible(@NotNull RuntimeException e) {
		if (e instanceof PersistenceException) {
			// Batch exceptions come inside another PersistenceException
			// recursion has a risk of infinite loop so better make another if
			var msg = e.getMessage();
			if (e.getCause() instanceof PersistenceException pe && msg == null) {
				msg = pe.getMessage();
			}
			if (e.getCause() instanceof SQLException se) {
				this.initExceptionTranslator();
				var task = e.getMessage() + "\n";
				var dae = this.exceptionTranslator.translate(task, null, se);
				return dae != null ? dae : new UncategorizedSQLException(task, null, se);
			}
			if (e.getCause() instanceof TransactionException te) {
				throw te;
			}
			return new MyBatisSystemException(msg, e);
		}
		return null;
	}

	/**
	 * Initializes the internal translator reference.
	 */
	private void initExceptionTranslator() {
		// 双检锁
		if (this.exceptionTranslator == null) {
			synchronized (lock) {
				if (this.exceptionTranslator == null) {
					SqlSessionFactory sessionFactory = SpringContextUtils.getBean(SqlSessionFactory.class);
					this.exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(
							sessionFactory.getConfiguration().getEnvironment().getDataSource());
				}
			}
		}
	}

}
