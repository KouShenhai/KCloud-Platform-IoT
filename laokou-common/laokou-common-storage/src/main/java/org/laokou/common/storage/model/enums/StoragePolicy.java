/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.storage.model.enums;

import lombok.Getter;
import org.laokou.common.i18n.util.EnumParser;
import org.laokou.common.storage.tdengine.TDengine;
import org.laokou.common.storage.tdengine.TDengineConfig;
import org.laokou.common.storage.model.valueobject.SourceV;
import org.laokou.common.storage.DataSource;

/**
 * @author laokou
 */
@Getter
public enum StoragePolicy {

	TIMESCALEDB("timescaledb", "TimescaleDB") {
		@Override
		public DataSource toDataSource(SourceV sourceV) {
			return null;
		}
	},

	CLICKHOUSE("clickhouse", "ClickHouse") {
		@Override
		public DataSource toDataSource(SourceV sourceV) {
			return null;
		}
	},

	IOTDB("iotdb", "IoTDB") {
		@Override
		public DataSource toDataSource(SourceV sourceV) {
			return null;
		}
	},

	INFLUXDB("influxdb", "InfluxDB") {
		@Override
		public DataSource toDataSource(SourceV sourceV) {
			return null;
		}
	},

	TDENGINE("tdengine", "TDengine") {
		@Override
		public DataSource toDataSource(SourceV sourceV) {
			TDengineConfig config = new TDengineConfig(sourceV.endpoint(), sourceV.username(), sourceV.password(),
					sourceV.dbName());
			return new TDengine(config);
		}
	};

	private final String code;

	private final String desc;

	StoragePolicy(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static StoragePolicy getByCode(String code) {
		return EnumParser.parse(StoragePolicy.class, StoragePolicy::getCode, code);
	}

	public abstract DataSource toDataSource(SourceV sourceV);

}
