/*
 *  Copyright 1999-2019 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.seata.server.metrics;

import io.seata.metrics.IdConstants;
import io.seata.metrics.Id;

/**
 * Constants for meter id in tc.
 *
 * @author zhengyangyong
 */
public interface MeterIdConstants {

	/**
	 * 活跃(次数).
	 */
	Id COUNTER_ACTIVE = new Id(IdConstants.SEATA_TRANSACTION).withTag(IdConstants.ROLE_KEY, IdConstants.ROLE_VALUE_TC)
		.withTag(IdConstants.METER_KEY, IdConstants.METER_VALUE_COUNTER)
		.withTag(IdConstants.STATUS_KEY, IdConstants.STATUS_VALUE_ACTIVE);

	/**
	 * 提交(次数).
	 */
	Id COUNTER_COMMITTED = new Id(IdConstants.SEATA_TRANSACTION)
		.withTag(IdConstants.ROLE_KEY, IdConstants.ROLE_VALUE_TC)
		.withTag(IdConstants.METER_KEY, IdConstants.METER_VALUE_COUNTER)
		.withTag(IdConstants.STATUS_KEY, IdConstants.STATUS_VALUE_COMMITTED);

	/**
	 * 回滚(次数).
	 */
	Id COUNTER_ROLLBACKED = new Id(IdConstants.SEATA_TRANSACTION)
		.withTag(IdConstants.ROLE_KEY, IdConstants.ROLE_VALUE_TC)
		.withTag(IdConstants.METER_KEY, IdConstants.METER_VALUE_COUNTER)
		.withTag(IdConstants.STATUS_KEY, IdConstants.STATUS_VALUE_ROLLBACKED);

	/**
	 * 修改回滚(次数).
	 */
	Id COUNTER_AFTER_ROLLBACKED = new Id(IdConstants.SEATA_TRANSACTION)
		.withTag(IdConstants.ROLE_KEY, IdConstants.ROLE_VALUE_TC)
		.withTag(IdConstants.METER_KEY, IdConstants.METER_VALUE_COUNTER)
		.withTag(IdConstants.STATUS_KEY, IdConstants.STATUS_VALUE_AFTER_ROLLBACKED_KEY);

	/**
	 * 修改提交(次数).
	 */
	Id COUNTER_AFTER_COMMITTED = new Id(IdConstants.SEATA_TRANSACTION)
		.withTag(IdConstants.ROLE_KEY, IdConstants.ROLE_VALUE_TC)
		.withTag(IdConstants.METER_KEY, IdConstants.METER_VALUE_COUNTER)
		.withTag(IdConstants.STATUS_KEY, IdConstants.STATUS_VALUE_AFTER_COMMITTED_KEY);

	/**
	 * 整体提交.
	 */
	Id SUMMARY_COMMITTED = new Id(IdConstants.SEATA_TRANSACTION)
		.withTag(IdConstants.ROLE_KEY, IdConstants.ROLE_VALUE_TC)
		.withTag(IdConstants.METER_KEY, IdConstants.METER_VALUE_SUMMARY)
		.withTag(IdConstants.STATUS_KEY, IdConstants.STATUS_VALUE_COMMITTED);

	/**
	 * 整体回滚.
	 */
	Id SUMMARY_ROLLBACKED = new Id(IdConstants.SEATA_TRANSACTION)
		.withTag(IdConstants.ROLE_KEY, IdConstants.ROLE_VALUE_TC)
		.withTag(IdConstants.METER_KEY, IdConstants.METER_VALUE_SUMMARY)
		.withTag(IdConstants.STATUS_KEY, IdConstants.STATUS_VALUE_ROLLBACKED);

	/**
	 * 整体失败.
	 */
	Id SUMMARY_FAILED = new Id(IdConstants.SEATA_TRANSACTION).withTag(IdConstants.ROLE_KEY, IdConstants.ROLE_VALUE_TC)
		.withTag(IdConstants.METER_KEY, IdConstants.METER_VALUE_SUMMARY)
		.withTag(IdConstants.STATUS_KEY, IdConstants.STATUS_VALUE_FAILED);

	/**
	 * 两次提交超时.
	 */
	Id SUMMARY_TWO_PHASE_TIMEOUT = new Id(IdConstants.SEATA_TRANSACTION)
		.withTag(IdConstants.ROLE_KEY, IdConstants.ROLE_VALUE_TC)
		.withTag(IdConstants.METER_KEY, IdConstants.METER_VALUE_SUMMARY)
		.withTag(IdConstants.STATUS_KEY, IdConstants.STATUS_VALUE_TWO_PHASE_TIMEOUT);

	/**
	 * 整体修改回滚.
	 */
	Id SUMMARY_AFTER_ROLLBACKED = new Id(IdConstants.SEATA_TRANSACTION)
		.withTag(IdConstants.ROLE_KEY, IdConstants.ROLE_VALUE_TC)
		.withTag(IdConstants.METER_KEY, IdConstants.METER_VALUE_SUMMARY)
		.withTag(IdConstants.STATUS_KEY, IdConstants.STATUS_VALUE_AFTER_ROLLBACKED_KEY);

	/**
	 * 整体修改提交.
	 */
	Id SUMMARY_AFTER_COMMITTED = new Id(IdConstants.SEATA_TRANSACTION)
		.withTag(IdConstants.ROLE_KEY, IdConstants.ROLE_VALUE_TC)
		.withTag(IdConstants.METER_KEY, IdConstants.METER_VALUE_SUMMARY)
		.withTag(IdConstants.STATUS_KEY, IdConstants.STATUS_VALUE_AFTER_COMMITTED_KEY);

	/**
	 * 提交.
	 */
	Id TIMER_COMMITTED = new Id(IdConstants.SEATA_TRANSACTION).withTag(IdConstants.ROLE_KEY, IdConstants.ROLE_VALUE_TC)
		.withTag(IdConstants.METER_KEY, IdConstants.METER_VALUE_TIMER)
		.withTag(IdConstants.STATUS_KEY, IdConstants.STATUS_VALUE_COMMITTED);

	/**
	 * 回滚.
	 */
	Id TIMER_ROLLBACK = new Id(IdConstants.SEATA_TRANSACTION).withTag(IdConstants.ROLE_KEY, IdConstants.ROLE_VALUE_TC)
		.withTag(IdConstants.METER_KEY, IdConstants.METER_VALUE_TIMER)
		.withTag(IdConstants.STATUS_KEY, IdConstants.STATUS_VALUE_ROLLBACKED);

	/**
	 * 失败.
	 */
	Id TIMER_FAILED = new Id(IdConstants.SEATA_TRANSACTION).withTag(IdConstants.ROLE_KEY, IdConstants.ROLE_VALUE_TC)
		.withTag(IdConstants.METER_KEY, IdConstants.METER_VALUE_TIMER)
		.withTag(IdConstants.STATUS_KEY, IdConstants.STATUS_VALUE_FAILED);

	/**
	 * 修改回滚.
	 */
	Id TIMER_AFTER_ROLLBACKED = new Id(IdConstants.SEATA_TRANSACTION)
		.withTag(IdConstants.ROLE_KEY, IdConstants.ROLE_VALUE_TC)
		.withTag(IdConstants.METER_KEY, IdConstants.METER_VALUE_TIMER)
		.withTag(IdConstants.STATUS_KEY, IdConstants.STATUS_VALUE_AFTER_ROLLBACKED_KEY);

	/**
	 * 修改提交.
	 */
	Id TIMER_AFTER_COMMITTED = new Id(IdConstants.SEATA_TRANSACTION)
		.withTag(IdConstants.ROLE_KEY, IdConstants.ROLE_VALUE_TC)
		.withTag(IdConstants.METER_KEY, IdConstants.METER_VALUE_TIMER)
		.withTag(IdConstants.STATUS_KEY, IdConstants.STATUS_VALUE_AFTER_COMMITTED_KEY);

	/**
	 * 整体异常.
	 */
	Id SUMMARY_EXP = new Id(IdConstants.SEATA_EXCEPTION).withTag(IdConstants.ROLE_KEY, IdConstants.ROLE_VALUE_TC)
		.withTag(IdConstants.METER_KEY, IdConstants.METER_VALUE_SUMMARY);

}
