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
import org.laokou.tenant.dto.SysTenantSourceDTO;
import org.laokou.admin.server.application.service.SysTenantSourceApplicationService;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.swagger.utils.HttpResult;
import org.laokou.tenant.qo.SysTenantSourceQo;
import org.laokou.tenant.vo.SysTenantSourceVO;
import org.springframework.web.bind.annotation.*;

/**
 * @author laokou
 */
@RestController
@RequestMapping("/sys/tenant/source/api")
@Tag(name = "Sys Tenant Source Api",description = "系统多租户数据源API")
@RequiredArgsConstructor
public class SysTenantSourceController {

    private final SysTenantSourceApplicationService sysTenantSourceApplicationService;

    @PostMapping("/query")
    @Operation(summary = "系统多租户数据源>查询",description = "系统多租户数据源>查询")
    public HttpResult<IPage<SysTenantSourceVO>> query(@RequestBody SysTenantSourceQo qo) {
        return new HttpResult<IPage<SysTenantSourceVO>>().ok(sysTenantSourceApplicationService.queryTenantSourcePage(qo));
    }

    @PostMapping("/insert")
    @Operation(summary = "系统多租户数据源>新增",description = "系统多租户数据源>新增")
    @OperateLog(module = "系统多租户数据源",name = "数据源新增")
    public HttpResult<Boolean> insert(@RequestBody SysTenantSourceDTO dto) {
        return new HttpResult<Boolean>().ok(sysTenantSourceApplicationService.insertTenantSource(dto));
    }

    @PutMapping("/update")
    @Operation(summary = "系统多租户数据源>修改",description = "系统多租户数据源>修改")
    @OperateLog(module = "系统多租户数据源",name = "数据源修改")
    public HttpResult<Boolean> update(@RequestBody SysTenantSourceDTO dto) {
        return new HttpResult<Boolean>().ok(sysTenantSourceApplicationService.updateTenantSource(dto));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "系统多租户数据源>删除",description = "系统多租户数据源>删除")
    @OperateLog(module = "系统多租户数据源",name = "数据源删除")
    public HttpResult<Boolean> delete(@RequestParam("id")Long id) {
        return new HttpResult<Boolean>().ok(sysTenantSourceApplicationService.deleteTenantSource(id));
    }

}
