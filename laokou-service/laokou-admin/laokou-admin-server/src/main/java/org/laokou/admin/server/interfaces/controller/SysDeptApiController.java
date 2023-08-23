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
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import lombok.RequiredArgsConstructor;
// import org.laokou.admin.server.application.service.SysDeptApplicationService;
// import org.laokou.admin.client.dto.SysDeptDTO;
// import org.laokou.admin.server.interfaces.qo.SysDeptQo;
// import org.laokou.common.data.cache.annotation.DataCache;
// import org.laokou.common.data.cache.enums.CacheEnum;
// import org.laokou.common.i18n.dto.Result;
// import org.laokou.admin.client.vo.SysDeptVO;
// import org.laokou.common.log.annotation.OperateLog;
// import org.laokou.common.trace.annotation.TraceLog;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.*;
// import java.util.*;
//
/// **
// * 系统部门
// *
// * @author laokou
// */
// @RestController
// @Tag(name = "Sys Dept API", description = "系统部门API")
// @RequestMapping("/sys/dept/api")
// @RequiredArgsConstructor
// public class SysDeptApiController {
//
// private final SysDeptApplicationService sysDeptApplicationService;
//
// @Operation(summary = "系统部门>树菜单", description = "系统部门>树菜单")
// @GetMapping("/tree")
// @TraceLog
// public Result<SysDeptVO> tree() {
// return Result.of(sysDeptApplicationService.treeDept());
// }
//
// @PostMapping("/query")
// @Operation(summary = "系统部门>查询", description = "系统部门>查询")
// @PreAuthorize("hasAuthority('sys:dept:query')")
// @TraceLog
// public Result<List<SysDeptVO>> query(@RequestBody SysDeptQo qo) {
// return Result.of(sysDeptApplicationService.queryDeptList(qo));
// }
//
// @PostMapping("/insert")
// @Operation(summary = "系统部门>新增", description = "系统部门>新增")
// @OperateLog(module = "系统部门", name = "部门新增")
// @PreAuthorize("hasAuthority('sys:dept:insert')")
// @TraceLog
// public Result<Boolean> insert(@RequestBody SysDeptDTO dto) {
// return Result.of(sysDeptApplicationService.insertDept(dto));
// }
//
// @PutMapping("/update")
// @Operation(summary = "系统部门>修改", description = "系统部门>修改")
// @OperateLog(module = "系统部门", name = "部门修改")
// @PreAuthorize("hasAuthority('sys:dept:update')")
// @TraceLog
// @DataCache(name = "dept", key = "#dto.id", type = CacheEnum.DEL)
// public Result<Boolean> update(@RequestBody SysDeptDTO dto) {
// return Result.of(sysDeptApplicationService.updateDept(dto));
// }
//
// @GetMapping("/detail")
// @TraceLog
// @Operation(summary = "系统部门>详情", description = "系统部门>详情")
// @DataCache(name = "dept", key = "#id")
// public Result<SysDeptVO> detail(@RequestParam("id") Long id) {
// return Result.of(sysDeptApplicationService.getDept(id));
// }
//
// @DeleteMapping("/delete")
// @TraceLog
// @Operation(summary = "系统部门>删除", description = "系统部门>删除")
// @OperateLog(module = "系统部门", name = "部门删除")
// @PreAuthorize("hasAuthority('sys:dept:delete')")
// @DataCache(name = "dept", key = "#id", type = CacheEnum.DEL)
// public Result<Boolean> delete(@RequestParam("id") Long id) {
// return Result.of(sysDeptApplicationService.deleteDept(id));
// }
//
// @GetMapping("/get")
// @TraceLog
// @Operation(summary = "系统部门>部门树ids", description = "系统部门>部门树ids")
// public Result<List<Long>> get(@RequestParam(value = "roleId") Long roleId) {
// return Result.of(sysDeptApplicationService.getDeptIdsByRoleId(roleId));
// }
//
// }
