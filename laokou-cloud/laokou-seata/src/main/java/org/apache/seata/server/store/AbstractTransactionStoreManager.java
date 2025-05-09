/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.seata.server.store;

import org.apache.seata.core.model.GlobalStatus;
import org.apache.seata.server.session.GlobalSession;
import org.apache.seata.server.session.SessionCondition;

import java.util.Collections;
import java.util.List;

/**
 * The type Abstract transaction store manager.
 *
 */
public abstract class AbstractTransactionStoreManager implements TransactionStoreManager {

	@Override
	public GlobalSession readSession(String xid) {
		return null;
	}

	@Override
	public GlobalSession readSession(String xid, boolean withBranchSessions) {
		return null;
	}

	@Override
	public List<GlobalSession> readSortByTimeoutBeginSessions(boolean withBranchSessions) {
		return Collections.emptyList();
	}

	@Override
	public List<GlobalSession> readSession(GlobalStatus[] statuses, boolean withBranchSessions) {
		return Collections.emptyList();
	}

	@Override
	public List<GlobalSession> readSession(SessionCondition sessionCondition) {
		return Collections.emptyList();
	}

	@Override
	public void shutdown() {
	}

}
