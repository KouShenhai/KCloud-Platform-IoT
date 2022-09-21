/**
 * Copyright 2020-2022 Kou Shenhai
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
 */
package io.laokou.admin.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.dto.SysDictDTO;
import io.laokou.admin.interfaces.qo.SysDictQO;
import io.laokou.admin.interfaces.vo.SysDictVO;

import javax.servlet.http.HttpServletRequest;

public interface SysDictApplicationService {

    IPage<SysDictVO> queryDictPage(SysDictQO qo);

    SysDictVO getDictById(Long id);

    Boolean insertDict(SysDictDTO dto, HttpServletRequest request);

    Boolean updateDict(SysDictDTO dto, HttpServletRequest request);

    Boolean deleteDict(Long id);

}
