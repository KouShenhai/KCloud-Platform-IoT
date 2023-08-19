///*
// * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *
// */
//package org.laokou.admin.server.interfaces.controller;
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.laokou.admin.client.dto.SysUserDTO;
//import org.laokou.admin.client.vo.SysUserOnlineVO;
//import org.laokou.admin.client.vo.UserInfoVO;
//import org.laokou.admin.server.interfaces.qo.SysUserOnlineQo;
//import org.laokou.admin.server.interfaces.qo.SysUserQo;
//import org.laokou.common.core.vo.OptionVO;
//import org.laokou.admin.client.vo.SysUserVO;
//import org.laokou.admin.server.application.service.SysUserApplicationService;
//import org.laokou.common.data.cache.annotation.DataCache;
//import org.laokou.common.data.cache.enums.CacheEnum;
//import org.laokou.common.i18n.dto.Result;
//import org.laokou.common.log.annotation.OperateLog;
//import org.laokou.common.trace.annotation.TraceLog;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
///**
// * @author laokou
// */
//@RestController
//@Tag(name = "Sys User Api", description = "系统用户API")
//@RequestMapping("user")
//@RequiredArgsConstructor
//public class SysUserApiController {
//
//	private final SysUserApplicationService sysUserApplicationService;
//
//	@TraceLog
//	@PutMapping("v1")
//	@Operation(summary = "修改", description = "修改")
//	@OperateLog(module = "系统用户", name = "修改")
//	@PreAuthorize("hasAuthority('user:update')")
//	@DataCache(name = "user", key = "#dto.id", type = CacheEnum.DEL)
//	public Result<Boolean> update(@RequestBody SysUserDTO dto) {
//		return Result.of(sysUserApplicationService.updateUser(dto));
//	}
//
//	@TraceLog
//	@PostMapping("v1/online_list")
//	@PreAuthorize("hasAuthority('user:online:list')")
//	@Operation(summary = "查询", description = "查询")
//	public Result<IPage<SysUserOnlineVO>> onlineList(@RequestBody SysUserOnlineQo qo) {
//		return Result.of(sysUserApplicationService.onlineQueryPage(qo));
//	}
//
//	@TraceLog
//	@DeleteMapping("v1/online_kill")
//	@Operation(summary = "强踢", description = "强踢")
//	@OperateLog(module = "在线用户", name = "强踢")
//	@PreAuthorize("hasAuthority('user:online:kill')")
//	public Result<Boolean> onlineKill(@RequestParam("token") String token) {
//		return Result.of(sysUserApplicationService.onlineKill(token));
//	}
//
//	@TraceLog
//	@GetMapping("v1/info")
//	@Operation(summary = "信息", description = "信息")
//	public Result<UserInfoVO> info() {
//		return Result.of(sysUserApplicationService.getUserInfo());
//	}
//
//	@TraceLog
//	@GetMapping("v1/option_list")
//	@Operation(summary = "列表", description = "列表")
//	public Result<List<OptionVO>> optionList() {
//		return Result.of(sysUserApplicationService.getOptionList());
//	}
//
//	@TraceLog
//	@PutMapping("v1/info")
//	@Operation(summary = "信息", description = "信息")
//	public Result<Boolean> info(@RequestBody SysUserDTO dto) {
//		return Result.of(sysUserApplicationService.updateInfo(dto));
//	}
//
//	@TraceLog
//	@PutMapping("v1/status/{id}/{status}")
//	@Operation(summary = "状态", description = "状态")
//	@OperateLog(module = "系统用户", name = "状态")
//	@PreAuthorize("hasAuthority('user:status')")
//	public Result<Boolean> status(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
//		return Result.of(sysUserApplicationService.updateStatus(id, status));
//	}
//
//	@TraceLog
//	@PutMapping("v1/pwd/{id}/{pwd}")
//	@Operation(summary = "密码", description = "密码")
//	@OperateLog(module = "系统用户", name = "密码")
//	@PreAuthorize("hasAuthority('user:pwd')")
//	public Result<Boolean> pwd(@PathVariable("id") Long id, @PathVariable("pwd") String pwd) {
//		return Result.of(sysUserApplicationService.updatePassword(id, pwd));
//	}
//
//	@TraceLog
//	@PutMapping("v1/info_pwd/{id}/{pwd}")
//	@Operation(summary = "密码", description = "密码")
//	public Result<Boolean> infoPwd(@PathVariable("id") Long id, @PathVariable("pwd") String pwd) {
//		return Result.of(sysUserApplicationService.updatePassword(id, pwd));
//	}
//
//	@TraceLog
//	@PostMapping("v1")
//	@Operation(summary = "新增", description = "新增")
//	@OperateLog(module = "系统用户", name = "新增")
//	@PreAuthorize("hasAuthority('user:insert')")
//	public Result<Boolean> insert(@RequestBody SysUserDTO dto) {
//		return Result.of(sysUserApplicationService.insertUser(dto));
//	}
//
//	@TraceLog
//	@GetMapping("v1/{id}")
//	@Operation(summary = "查看", description = "查看")
//	@DataCache(name = "user", key = "#id")
//	public Result<SysUserVO> get(@PathVariable("id") Long id) {
//		return Result.of(sysUserApplicationService.getUserById(id));
//	}
//
//	@TraceLog
//	@DeleteMapping("v1")
//	@Operation(summary = "删除", description = "删除")
//	@OperateLog(module = "系统用户", name = "删除")
//	@PreAuthorize("hasAuthority('user:delete')")
//	@DataCache(name = "user", key = "#id", type = CacheEnum.DEL)
//	public Result<Boolean> delete(@RequestParam("id") Long id) {
//		return Result.of(sysUserApplicationService.deleteUser(id));
//	}
//
//	@TraceLog
//	@PostMapping("v1/list")
//	@Operation(summary = "查询", description = "查询")
//	@PreAuthorize("hasAuthority('user:list')")
//	public Result<IPage<SysUserVO>> list(@RequestBody SysUserQo qo) {
//		return Result.of(sysUserApplicationService.queryUserPage(qo));
//	}
//
//}
