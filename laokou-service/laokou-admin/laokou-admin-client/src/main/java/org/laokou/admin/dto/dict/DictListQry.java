/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.admin.dto.dict;

import lombok.Data;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.StringUtil;

/**
 * @author laokou
 */
@Data
public class DictListQry extends PageQuery {

	private String type;

	private String label;

	public void setType(String type) {
		this.type = StringUtil.like(type);
	}

	public void setLabel(String label) {
		this.label = StringUtil.like(label);
	}

}
