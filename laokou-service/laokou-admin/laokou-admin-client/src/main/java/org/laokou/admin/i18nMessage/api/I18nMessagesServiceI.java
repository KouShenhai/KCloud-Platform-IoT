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

package org.laokou.admin.i18nMessage.api;

import org.laokou.admin.i18nMessage.dto.*;
import org.laokou.admin.i18nMessage.dto.clientobject.I18nMessageCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;

/**
 * 国际化消息接口.
 *
 * @author laokou
 */
public interface I18nMessagesServiceI {

	/**
	 * 保存国际化消息.
	 * @param cmd 保存命令
	 */
	void saveI18nMessage(I18nMessageSaveCmd cmd);

	/**
	 * 修改国际化消息.
	 * @param cmd 修改命令
	 */
	void modifyI18nMessage(I18nMessageModifyCmd cmd);

	/**
	 * 删除国际化消息.
	 * @param cmd 删除命令
	 */
	void removeI18nMessage(I18nMessageRemoveCmd cmd);

	/**
	 * 导入国际化消息.
	 * @param cmd 导入命令
	 */
	void importI18nMessage(I18nMessageImportCmd cmd);

	/**
	 * 导出国际化消息.
	 * @param cmd 导出命令
	 */
	void exportI18nMessage(I18nMessageExportCmd cmd);

	/**
	 * 分页查询国际化消息.
	 * @param qry 分页查询请求
	 */
	Result<Page<I18nMessageCO>> pageI18nMessage(I18nMessagePageQry qry);

	/**
	 * 查看国际化消息.
	 * @param qry 查看请求
	 */
	Result<I18nMessageCO> getByIdI18nMessage(I18nMessageGetQry qry);

}
