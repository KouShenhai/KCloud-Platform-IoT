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

package org.laokou.common.excel;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.laokou.common.mybatisplus.mapper.BaseDO;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author laokou
 */
@Data
@TableName("t_user")
class TestUserDO extends BaseDO {

	private String name;

	@Test
	void test() {
		TestUserDO user = new TestUserDO();
		user.setId(1L);
		assertThat(user.getId()).isEqualTo(1L);
	}

}
