/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
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
import org.laokou.admin.client.constant.CacheConstant;
import org.laokou.admin.client.enums.CacheEnum;
import org.laokou.admin.server.application.service.SysRoleApplicationService;
import org.laokou.admin.client.dto.SysRoleDTO;
import org.laokou.admin.server.infrastructure.annotation.DataCache;
import org.laokou.admin.server.interfaces.qo.SysRoleQo;
import org.laokou.admin.client.vo.SysRoleVO;
import org.laokou.admin.server.infrastructure.annotation.OperateLog;
import org.laokou.common.swagger.utils.HttpResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 系统角色控制器
 * @author laokou
 */
@RestController
@Tag(name = "Sys Role Api",description = "系统角色API")
@RequiredArgsConstructor
@RequestMapping("/sys/role/api")
public class SysRoleApiController {

    private final SysRoleApplicationService sysRoleApplicationService;

    @PostMapping("/query")
    @Operation(summary = "系统角色>查询",description = "系统角色>查询")
    @PreAuthorize("hasAuthority('sys:role:query')")
    public HttpResult<IPage<SysRoleVO>> query(@RequestBody SysRoleQo qo) {
        return new HttpResult<IPage<SysRoleVO>>().ok(sysRoleApplicationService.queryRolePage(qo));
    }

    @PostMapping("/list")
    @Operation(summary = "系统角色>列表",description = "系统角色>列表")
    public HttpResult<List<SysRoleVO>> list(@RequestBody SysRoleQo qo) {
        return new HttpResult<List<SysRoleVO>>().ok(sysRoleApplicationService.getRoleList(qo));
    }

    @GetMapping("/detail")
    @Operation(summary = "系统角色>详情",description = "系统角色>详情")
    @DataCache(name = CacheConstant.ROLE,key = "#id")
    public HttpResult<SysRoleVO> detail(@RequestParam("id") Long id) {
        return new HttpResult<SysRoleVO>().ok(sysRoleApplicationService.getRoleById(id));
    }

    @PostMapping("/insert")
    @Operation(summary = "系统角色>新增",description = "系统角色>新增")
    @OperateLog(module = "系统角色",name = "角色新增")
    @PreAuthorize("hasAuthority('sys:role:insert')")
    public HttpResult<Boolean> insert(@RequestBody SysRoleDTO dto) {
        return new HttpResult<Boolean>().ok(sysRoleApplicationService.insertRole(dto));
    }

    @PutMapping("/update")
    @Operation(summary = "系统角色>修改",description = "系统角色>修改")
    @OperateLog(module = "系统角色",name = "角色修改")
    @PreAuthorize("hasAuthority('sys:role:update')")
    @DataCache(name = CacheConstant.ROLE,key = "#dto.id",type = CacheEnum.DEL)
    public HttpResult<Boolean> update(@RequestBody SysRoleDTO dto) {
        return new HttpResult<Boolean>().ok(sysRoleApplicationService.updateRole(dto));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "系统角色>删除",description = "系统角色>删除")
    @OperateLog(module = "系统角色",name = "角色删除")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @DataCache(name = CacheConstant.ROLE,key = "#id",type = CacheEnum.DEL)
    public HttpResult<Boolean> delete(@RequestParam("id") Long id) {
        return new HttpResult<Boolean>().ok(sysRoleApplicationService.deleteRole(id));
    }

}
