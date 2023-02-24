/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.admin.server.interfaces.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.client.dto.SysOssDTO;
import org.laokou.admin.server.application.service.SysOssApplicationService;
import org.laokou.admin.server.interfaces.qo.SysOssQo;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.data.cache.enums.CacheEnum;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.i18n.core.HttpResult;
import org.laokou.oss.client.vo.SysOssVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author laokou
 */
@RestController
@RequestMapping("/sys/oss/api")
@Tag(name = "Sys Oss Api",description = "系统存储API")
@RequiredArgsConstructor
public class SysOssApiController {

    private final SysOssApplicationService sysOssApplicationService;

    @PostMapping("/query")
    @Operation(summary = "系统存储>查询",description = "系统存储>查询")
    @PreAuthorize("hasAuthority('sys:oss:query')")
    public HttpResult<IPage<SysOssVO>> query(@RequestBody SysOssQo qo) {
        return new HttpResult<IPage<SysOssVO>>().ok(sysOssApplicationService.queryOssPage(qo));
    }

    @PostMapping("/insert")
    @Operation(summary = "系统存储>新增",description = "系统存储>新增")
    @OperateLog(module = "系统存储",name = "存储新增")
    @PreAuthorize("hasAuthority('sys:oss:insert')")
    public HttpResult<Boolean> insert(@RequestBody SysOssDTO dto) {
        return new HttpResult<Boolean>().ok(sysOssApplicationService.insertOss(dto));
    }

    @GetMapping("/use")
    @Operation(summary = "系统存储>启用",description = "系统存储>启用")
    @OperateLog(module = "系统存储",name = "存储启用")
    @PreAuthorize("hasAuthority('sys:oss:use')")
    public HttpResult<Boolean> use(@RequestParam("id") Long id) {
        return new HttpResult<Boolean>().ok(sysOssApplicationService.useOss(id));
    }

    @GetMapping("/detail")
    @Operation(summary = "系统存储>查看",description = "系统存储>查看")
    @DataCache(name = "oss",key = "#id")
    public HttpResult<SysOssVO> detail(@RequestParam("id")Long id) {
        return new HttpResult<SysOssVO>().ok(sysOssApplicationService.getOssById(id));
    }

    @PutMapping("/update")
    @Operation(summary = "系统存储>修改",description = "系统存储>修改")
    @OperateLog(module = "系统存储",name = "存储修改")
    @PreAuthorize("hasAuthority('sys:oss:update')")
    @DataCache(name = "oss",key = "#dto.id",type = CacheEnum.DEL)
    public HttpResult<Boolean> update(@RequestBody SysOssDTO dto) {
        return new HttpResult<Boolean>().ok(sysOssApplicationService.updateOss(dto));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "系统存储>删除",description = "系统存储>删除")
    @OperateLog(module = "系统存储",name = "存储删除")
    @PreAuthorize("hasAuthority('sys:oss:delete')")
    @DataCache(name = "oss",key = "#id",type = CacheEnum.DEL)
    public HttpResult<Boolean> delete(@RequestParam("id")Long id) {
        return new HttpResult<Boolean>().ok(sysOssApplicationService.deleteOss(id));
    }

}
