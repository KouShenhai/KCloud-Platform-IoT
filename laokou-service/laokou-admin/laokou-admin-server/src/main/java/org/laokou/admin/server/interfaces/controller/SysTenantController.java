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
import org.laokou.admin.server.application.service.SysTenantApplicationService;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.data.cache.enums.CacheEnum;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.swagger.utils.HttpResult;
import org.laokou.tenant.dto.SysTenantDTO;
import org.laokou.tenant.qo.SysTenantQo;
import org.laokou.tenant.vo.SysTenantVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
/**
 * @author laokou
 */
@RestController
@RequestMapping("/sys/tenant/api")
@Tag(name = "Sys Tenant Api",description = "系统租户API")
@RequiredArgsConstructor
public class SysTenantController {

    private final SysTenantApplicationService sysTenantApplicationService;

    @PostMapping("/query")
    @Operation(summary = "系统租户>查询",description = "系统租户>查询")
    @PreAuthorize("hasAuthority('sys:tenant:query')")
    public HttpResult<IPage<SysTenantVO>> query(@RequestBody SysTenantQo qo) {
        return new HttpResult<IPage<SysTenantVO>>().ok(sysTenantApplicationService.queryTenantPage(qo));
    }

    @PostMapping("/insert")
    @Operation(summary = "系统租户>新增",description = "系统租户>新增")
    @OperateLog(module = "系统租户",name = "租户新增")
    @PreAuthorize("hasAuthority('sys:tenant:insert')")
    public HttpResult<Boolean> insert(@RequestBody SysTenantDTO dto) {
        return new HttpResult<Boolean>().ok(sysTenantApplicationService.insertTenant(dto));
    }

    @GetMapping("/detail")
    @Operation(summary = "系统租户>查看",description = "系统租户>查看")
    @DataCache(name = "tenant",key = "#id")
    public HttpResult<SysTenantVO> detail(@RequestParam("id")Long id) {
        return new HttpResult<SysTenantVO>().ok(sysTenantApplicationService.getTenantById(id));
    }

    @PutMapping("/update")
    @Operation(summary = "系统租户>修改",description = "系统租户>修改")
    @OperateLog(module = "系统租户",name = "租户修改")
    @PreAuthorize("hasAuthority('sys:tenant:update')")
    @DataCache(name = "tenant",key = "#dto.id",type = CacheEnum.DEL)
    public HttpResult<Boolean> update(@RequestBody SysTenantDTO dto) {
        return new HttpResult<Boolean>().ok(sysTenantApplicationService.updateTenant(dto));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "系统租户>删除",description = "系统租户>删除")
    @OperateLog(module = "系统租户",name = "租户删除")
    @PreAuthorize("hasAuthority('sys:tenant:delete')")
    @DataCache(name = "tenant",key = "#id",type = CacheEnum.DEL)
    public HttpResult<Boolean> delete(@RequestParam("id")Long id) {
        return new HttpResult<Boolean>().ok(sysTenantApplicationService.deleteTenant(id));
    }

}
