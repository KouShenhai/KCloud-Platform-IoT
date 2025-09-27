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

package org.laokou.oss.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.CommonCommand;
import org.laokou.oss.dto.clientobject.OssLogCO;

/**
 * 保存OSS日志命令.
 *
 * @author laokou
 */
@Getter
@RequiredArgsConstructor
public class OssLogSaveCmd extends CommonCommand {

	private final OssLogCO co;

}
