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

package org.laokou.common.core;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.ServerUtils;
import java.net.UnknownHostException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author laokou
 */
class ServerUtilsTest {

	@Test
	void test_getServerInfo() throws UnknownHostException {
		ServerUtils.Server serverInfo = ServerUtils.getServerInfo();
		assertThat(serverInfo.getSys().getOsName()).isEqualTo("Windows 11");
		assertThat(serverInfo.getSys().getOsArch()).isEqualTo("amd64");
	}

}
