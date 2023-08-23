/// *
// * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *
// */
// package org.laokou.admin.server.interfaces.controller;
//
// import com.baomidou.mybatisplus.core.metadata.IPage;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import lombok.RequiredArgsConstructor;
// import org.laokou.admin.server.application.service.SysRoleApplicationService;
// import org.laokou.admin.client.dto.SysRoleDTO;
// import org.laokou.admin.server.interfaces.qo.SysRoleQo;
// import org.laokou.admin.client.vo.SysRoleVO;
// import org.laokou.common.data.cache.annotation.DataCache;
// import org.laokou.common.data.cache.enums.CacheEnum;
// import org.laokou.common.i18n.dto.Result;
// import org.laokou.common.log.annotation.OperateLog;
// import org.laokou.common.trace.annotation.TraceLog;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.*;
// import java.util.List;
//
/// **
// * 系统角色控制器
// *
// * @author laokou
// */
// @RestController
// @Tag(name = "Sys Role Api", description = "系统角色API")
// @RequiredArgsConstructor
// @RequestMapping("/sys/role/api")
// public class SysRoleApiController {
//
// private final SysRoleApplicationService sysRoleApplicationService;
//
// @TraceLog
// @PostMapping("/query")
// @Operation(summary = "系统角色>查询", description = "系统角色>查询")
// @PreAuthorize("hasAuthority('sys:role:query')")
// public Result<IPage<SysRoleVO>> query(@RequestBody SysRoleQo qo) {
// return Result.of(sysRoleApplicationService.queryRolePage(qo));
// }
//
// @TraceLog
// @PostMapping("/list")
// @Operation(summary = "系统角色>列表", description = "系统角色>列表")
// public Result<List<SysRoleVO>> list(@RequestBody SysRoleQo qo) {
// return Result.of(sysRoleApplicationService.getRoleList(qo));
// }
//
// @TraceLog
// @GetMapping("/detail")
// @Operation(summary = "系统角色>详情", description = "系统角色>详情")
// @DataCache(name = "role", key = "#id")
// public Result<SysRoleVO> detail(@RequestParam("id") Long id) {
// return Result.of(sysRoleApplicationService.getRoleById(id));
// }
//
// @TraceLog
// @PostMapping("/insert")
// @Operation(summary = "系统角色>新增", description = "系统角色>新增")
// @OperateLog(module = "系统角色", name = "角色新增")
// @PreAuthorize("hasAuthority('sys:role:insert')")
// public Result<Boolean> insert(@RequestBody SysRoleDTO dto) {
// return Result.of(sysRoleApplicationService.insertRole(dto));
// }
//
// @TraceLog
// @PutMapping("/update")
// @Operation(summary = "系统角色>修改", description = "系统角色>修改")
// @OperateLog(module = "系统角色", name = "角色修改")
// @PreAuthorize("hasAuthority('sys:role:update')")
// @DataCache(name = "role", key = "#dto.id", type = CacheEnum.DEL)
// public Result<Boolean> update(@RequestBody SysRoleDTO dto) {
// return Result.of(sysRoleApplicationService.updateRole(dto));
// }
//
// @TraceLog
// @DeleteMapping("/delete")
// @Operation(summary = "系统角色>删除", description = "系统角色>删除")
// @OperateLog(module = "系统角色", name = "角色删除")
// @PreAuthorize("hasAuthority('sys:role:delete')")
// @DataCache(name = "role", key = "#id", type = CacheEnum.DEL)
// public Result<Boolean> delete(@RequestParam("id") Long id) {
// return Result.of(sysRoleApplicationService.deleteRole(id));
// }
//
// }
