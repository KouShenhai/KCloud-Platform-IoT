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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.server.application.service.SysMenuApplicationService;
import org.laokou.admin.client.dto.SysMenuDTO;
import org.laokou.admin.server.interfaces.qo.SysMenuQo;
import org.laokou.admin.client.vo.SysMenuVO;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.data.cache.enums.CacheEnum;
import org.laokou.common.swagger.utils.HttpResult;
import org.laokou.common.log.annotation.OperateLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 系统菜单控制器
 * @author laokou
 */
@RestController
@Tag(name = "Sys Menu API",description = "系统菜单API")
@RequestMapping("/sys/menu/api")
@RequiredArgsConstructor
public class SysMenuApiController {

    private final SysMenuApplicationService sysMenuApplicationService;

    @GetMapping("/list")
    @Operation(summary = "系统菜单>列表",description = "系统菜单>列表")
    public HttpResult<SysMenuVO> list() {
        return new HttpResult<SysMenuVO>().ok(sysMenuApplicationService.getMenuList());
    }

    @PostMapping("/query")
    @Operation(summary = "系统菜单>查询",description = "系统菜单>查询")
    @PreAuthorize("hasAuthority('sys:menu:query')")
    public HttpResult<List<SysMenuVO>> query(@RequestBody SysMenuQo qo) {
        return new HttpResult<List<SysMenuVO>>().ok(sysMenuApplicationService.queryMenuList(qo));
    }

    @GetMapping("/detail")
    @Operation(summary = "系统菜单>详情",description = "系统菜单>详情")
    @DataCache(name = "menu", key = "#id")
    public HttpResult<SysMenuVO> detail(@RequestParam("id")Long id) {
        return new HttpResult<SysMenuVO>().ok(sysMenuApplicationService.getMenuById(id));
    }

    @PutMapping("/update")
    @Operation(summary = "系统菜单>修改",description = "系统菜单>修改")
    @OperateLog(module = "系统菜单",name = "菜单修改")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    @DataCache(name = "menu", key = "#dto.id",type = CacheEnum.DEL)
    public HttpResult<Boolean> update(@RequestBody SysMenuDTO dto) {
        return new HttpResult<Boolean>().ok(sysMenuApplicationService.updateMenu(dto));
    }

    @PostMapping("/insert")
    @Operation(summary = "系统菜单>新增",description = "系统菜单>新增")
    @OperateLog(module = "系统菜单",name = "菜单新增")
    @PreAuthorize("hasAuthority('sys:menu:insert')")
    public HttpResult<Boolean> insert(@RequestBody SysMenuDTO dto) {
        return new HttpResult<Boolean>().ok(sysMenuApplicationService.insertMenu(dto));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "系统菜单>删除",description = "系统菜单>删除")
    @OperateLog(module = "系统菜单",name = "菜单删除")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    @DataCache(name = "menu", key = "#id",type = CacheEnum.DEL)
    public HttpResult<Boolean> delete(@RequestParam("id") Long id) {
        return new HttpResult<Boolean>().ok(sysMenuApplicationService.deleteMenu(id));
    }

    @GetMapping("/tree")
    @Operation(summary = "系统菜单>树菜单",description = "系统菜单>树菜单")
    public HttpResult<SysMenuVO> tree(@RequestParam(required = false,value = "roleId")Long roleId) {
        return new HttpResult<SysMenuVO>().ok(sysMenuApplicationService.treeMenu(roleId));
    }

    @GetMapping("/get")
    @Operation(summary = "系统菜单>菜单树ids",description = "系统菜单>菜单树ids")
    public HttpResult<List<Long>> get(@RequestParam(value = "roleId")Long roleId) {
        return new HttpResult<List<Long>>().ok(sysMenuApplicationService.getMenuIdsByRoleId(roleId));
    }

}
