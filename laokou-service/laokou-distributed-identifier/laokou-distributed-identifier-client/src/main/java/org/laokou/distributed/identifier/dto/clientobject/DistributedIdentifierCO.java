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

package org.laokou.distributed.identifier.dto.clientobject;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.dto.ClientObject;

import java.time.Instant;

import static org.laokou.common.i18n.util.DateUtils.UTC_TIMEZONE;
import static org.laokou.common.i18n.util.DateUtils.YYYY_B_MM_B_DD_T_HH_R_MM_R_SS_D_SSS_Z;

/**
 * @author laokou
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistributedIdentifierCO extends ClientObject {

	private Long id;

	@JsonFormat(pattern = YYYY_B_MM_B_DD_T_HH_R_MM_R_SS_D_SSS_Z, timezone = UTC_TIMEZONE)
	private Instant time;

}
