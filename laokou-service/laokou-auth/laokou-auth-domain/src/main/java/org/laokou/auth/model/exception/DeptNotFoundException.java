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

package org.laokou.auth.model.exception;

import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.util.I18nUtils;
import org.laokou.common.i18n.util.MessageUtils;

/**
 * @author laokou
 */
public final class DeptNotFoundException extends BizException {

	public DeptNotFoundException(String code) {
		super(code, MessageUtils.getMessage(code, I18nUtils.getLocale()));
	}

}
