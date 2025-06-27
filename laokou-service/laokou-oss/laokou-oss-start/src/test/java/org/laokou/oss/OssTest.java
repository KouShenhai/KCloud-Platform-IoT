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

package org.laokou.oss;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.UUIDGenerator;
import org.laokou.common.i18n.util.ResourceUtils;
import org.laokou.oss.api.OssServiceI;
import org.laokou.oss.dto.OssUploadCmd;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

/**
 * @author laokou
 */
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OssTest {

	private final OssServiceI ossServiceI;

	@Test
	void testOssUpload() throws Exception {
		byte[] bytes = ResourceUtils.getResource("classpath:1.jpg").getInputStream().readAllBytes();
		ossServiceI.uploadOss(new OssUploadCmd("image", bytes, UUIDGenerator.generateUUID() + ".jpg", ".jpg",
				"image/jpeg", bytes.length));
	}

}
