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

package org.laokou.common.core.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import tools.jackson.datatype.jsr310.ser.InstantSerializer;

import java.time.format.DateTimeFormatter;

/**
 * @author laokou
 */
public final class CustomInstantSerializer extends InstantSerializer {

	public CustomInstantSerializer(InstantSerializer base, DateTimeFormatter formatter, Boolean useTimestamp,
			Boolean useNanoseconds) {
		super(base, formatter, useTimestamp, useNanoseconds, JsonFormat.Shape.ANY);
	}

}
