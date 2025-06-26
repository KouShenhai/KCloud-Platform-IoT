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

package org.laokou.common.oss.template;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.oss.model.BaseOss;
import org.laokou.common.oss.model.FileInfo;
import org.laokou.common.oss.model.LoadBalancePolicyEnum;

import java.util.List;

/**
 * @author laokou
 */
@RequiredArgsConstructor
public class StorageTemplate {

	public Result<String> uploadOss(FileInfo fileInfo, List<BaseOss> list) throws Exception {
		BaseOss baseOss = LoadBalancePolicyEnum.HASH.choose(list);
		return Result.ok(baseOss.getStoragePolicy().getStorage(fileInfo, baseOss).uploadOss());
	}

}
