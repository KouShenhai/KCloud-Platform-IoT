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
// import org.laokou.admin.server.application.service.SysDictApplicationService;
// import org.laokou.admin.client.dto.SysDictDTO;
// import org.laokou.admin.server.interfaces.qo.SysDictQo;
// import org.laokou.admin.client.vo.SysDictVO;
// import org.laokou.common.core.vo.OptionVO;
// import org.laokou.common.data.cache.annotation.DataCache;
// import org.laokou.common.data.cache.enums.CacheEnum;
// import org.laokou.common.i18n.dto.Result;
// import org.laokou.common.log.annotation.OperateLog;
// import org.laokou.common.trace.annotation.TraceLog;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.*;
//
// import java.util.List;
//
/// **
// * 系统字典控制器
// *
// * @author laokou
// */
// @RestController
// @Tag(name = "Sys Dict API", description = "系统字典API")
// @RequestMapping("/sys/dict/api")
// @RequiredArgsConstructor
// public class SysDictApiController {
//
// private final SysDictApplicationService sysDictApplicationService;
//
// @PostMapping(value = "/query")
// @TraceLog
// @Operation(summary = "系统字典>查询", description = "系统字典>查询")
// @PreAuthorize("hasAuthority('sys:dict:query')")
// public Result<IPage<SysDictVO>> query(@RequestBody SysDictQo qo) {
// return Result.of(sysDictApplicationService.queryDictPage(qo));
// }
//
// @TraceLog
// @GetMapping("/option/list")
// @Operation(summary = "系统字典>下拉框列表", description = "系统字典>下拉框列表")
// public Result<List<OptionVO>> optionList(@RequestParam("type") String type) {
// return Result.of(sysDictApplicationService.getOptionList(type));
// }
//
// @TraceLog
// @GetMapping(value = "/detail")
// @Operation(summary = "系统字典>详情", description = "系统字典>详情")
// @DataCache(name = "dict", key = "#id")
// public Result<SysDictVO> detail(@RequestParam("id") Long id) {
// return Result.of(sysDictApplicationService.getDictById(id));
// }
//
// @TraceLog
// @PostMapping(value = "/insert")
// @Operation(summary = "系统字典>新增", description = "系统字典>新增")
// @OperateLog(module = "系统字典", name = "字典新增")
// @PreAuthorize("hasAuthority('sys:dict:insert')")
// public Result<Boolean> insert(@RequestBody SysDictDTO dto) {
// return Result.of(sysDictApplicationService.insertDict(dto));
// }
//
// @TraceLog
// @PutMapping(value = "/update")
// @Operation(summary = "系统字典>修改", description = "系统字典>修改")
// @OperateLog(module = "系统字典", name = "字典修改")
// @PreAuthorize("hasAuthority('sys:dict:update')")
// @DataCache(name = "dict", key = "#dto.id", type = CacheEnum.DEL)
// public Result<Boolean> update(@RequestBody SysDictDTO dto) {
// return Result.of(sysDictApplicationService.updateDict(dto));
// }
//
// @TraceLog
// @DeleteMapping(value = "/delete")
// @Operation(summary = "系统字典>删除", description = "系统字典>删除")
// @OperateLog(module = "系统字典", name = "字典删除")
// @PreAuthorize("hasAuthority('sys:dict:delete')")
// @DataCache(name = "dict", key = "#id", type = CacheEnum.DEL)
// public Result<Boolean> delete(@RequestParam("id") Long id) {
// return Result.of(sysDictApplicationService.deleteDict(id));
// }
//
// }
