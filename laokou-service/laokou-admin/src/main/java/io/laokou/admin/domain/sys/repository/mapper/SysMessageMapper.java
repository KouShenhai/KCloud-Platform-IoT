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
package io.laokou.admin.domain.sys.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.domain.sys.entity.SysMessageDO;
import io.laokou.admin.interfaces.qo.SysMessageQO;
import io.laokou.admin.interfaces.vo.MessageDetailVO;
import io.laokou.admin.interfaces.vo.SysMessageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SysMessageMapper extends BaseMapper<SysMessageDO> {

    IPage<SysMessageVO> getMessageList(IPage<SysMessageVO> page, @Param("qo") SysMessageQO qo);

    MessageDetailVO getMessageByDetailId(@Param("id") Long id);

    IPage<SysMessageVO> getUnReadList(IPage<SysMessageVO> page, @Param("userId") Long userId);

    void readMessage(Long id);

    MessageDetailVO getMessageById(@Param("id")Long id);
}
