/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.common.i18n.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * This is the object communicate with Client. The clients could be view layer or other
 * HSF Consumers.
 *
 * @author fulan.zjf 2017-10-27 PM 12:19:15
 */
public abstract class ClientObject implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

}
