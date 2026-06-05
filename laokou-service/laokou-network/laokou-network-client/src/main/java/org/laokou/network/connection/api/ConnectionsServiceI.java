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

package org.laokou.network.connection.api;

import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.network.connection.dto.ConnectionGetQry;
import org.laokou.network.connection.dto.ConnectionModifyCmd;
import org.laokou.network.connection.dto.ConnectionPageQry;
import org.laokou.network.connection.dto.ConnectionRemoveCmd;
import org.laokou.network.connection.dto.ConnectionSaveCmd;
import org.laokou.network.connection.dto.clientobject.ConnectionCO;

/**
 * Network connection service interface.
 *
 * @author laokou
 */
public interface ConnectionsServiceI {

	void saveConnection(ConnectionSaveCmd cmd);

	void modifyConnection(ConnectionModifyCmd cmd);

	void removeConnection(ConnectionRemoveCmd cmd);

	Result<Page<ConnectionCO>> pageConnection(ConnectionPageQry qry);

	Result<ConnectionCO> getConnectionById(ConnectionGetQry qry);

}
