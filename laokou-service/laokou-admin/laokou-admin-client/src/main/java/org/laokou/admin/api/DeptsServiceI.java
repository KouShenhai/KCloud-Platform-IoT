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
package org.laokou.admin.api;

import org.laokou.admin.dto.dept.*;
import org.laokou.admin.dto.dept.clientobject.DeptCO;
import org.laokou.common.i18n.dto.Result;

import java.util.List;

/**
 * @author laokou
 */
public interface DeptsServiceI {

	Result<DeptCO> tree(DeptTreeGetQry qry);

	Result<List<DeptCO>> list(DeptListQry qry);

	Result<Boolean> insert(DeptInsertCmd cmd);

	Result<Boolean> update(DeptUpdateCmd cmd);

	Result<Boolean> deleteById(DeptDeleteCmd cmd);

	Result<DeptCO> getById(DeptGetQry qry);

	Result<List<Long>> ids(DeptIDSGetQry qry);

}
