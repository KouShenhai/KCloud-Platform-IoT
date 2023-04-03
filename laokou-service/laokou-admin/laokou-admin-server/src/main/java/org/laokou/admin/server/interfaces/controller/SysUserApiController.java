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
import org.laokou.admin.client.dto.SysUserDTO;
import org.laokou.admin.client.vo.UserInfoVO;
import org.laokou.admin.server.interfaces.qo.SysUserQo;
import org.laokou.common.core.vo.OptionVO;
import org.laokou.admin.client.vo.SysUserVO;
import org.laokou.admin.server.application.service.SysUserApplicationService;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.data.cache.enums.CacheEnum;
import org.laokou.common.i18n.core.HttpResult;
import org.laokou.common.log.annotation.OperateLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 系统用户控制器
 * @author laokou
 */
@RestController
@Tag(name = "Sys User Api",description = "系统用户API")
@RequestMapping("/sys/user/api")
@RequiredArgsConstructor
public class SysUserApiController {

    private final SysUserApplicationService sysUserApplicationService;

    @PutMapping("/update")
    @Operation(summary = "系统用户>修改",description = "系统用户>修改")
    @OperateLog(module = "系统用户",name = "用户修改")
    @PreAuthorize("hasAuthority('sys:user:update')")
    @DataCache(name = "user", key = "#dto.id", type = CacheEnum.DEL)
    public HttpResult<Boolean> update(@RequestBody SysUserDTO dto) {
        return new HttpResult<Boolean>().ok(sysUserApplicationService.updateUser(dto));
    }

    @PostMapping("/online/query")
    public void test() {

    }

    @PostMapping("/online/kill")
    public void kill() {

    }

    @GetMapping("/userInfo")
    @Operation(summary = "系统用户>用户信息",description = "系统用户>用户信息")
    public HttpResult<UserInfoVO> userInfo() {
        return new HttpResult<UserInfoVO>().ok(sysUserApplicationService.getUserInfo());
    }

    @GetMapping("/option/list")
    @Operation(summary = "系统用户>下拉框列表",description = "系统用户>下拉框列表")
    public HttpResult<List<OptionVO>> optionList() {
        return new HttpResult<List<OptionVO>>().ok(sysUserApplicationService.getOptionList());
    }

    @PutMapping("/info")
    @Operation(summary = "系统用户>修改个人信息",description = "系统用户>修改个人信息")
    public HttpResult<Boolean> info(@RequestBody SysUserDTO dto) {
        return new HttpResult<Boolean>().ok(sysUserApplicationService.updateInfo(dto));
    }

    @PutMapping("/status")
    @Operation(summary = "系统用户>修改用户状态",description = "系统用户>修改用户状态")
    @OperateLog(module = "系统用户",name = "用户状态")
    @PreAuthorize("hasAuthority('sys:user:status')")
    public HttpResult<Boolean> status(@RequestParam("id")Long id,@RequestParam("status")Integer status) {
        return new HttpResult<Boolean>().ok(sysUserApplicationService.updateStatus(id,status));
    }

    @PutMapping("/password")
    @Operation(summary = "系统用户>重置密码",description = "系统用户>重置密码")
    @OperateLog(module = "系统用户",name = "重置密码")
    @PreAuthorize("hasAuthority('sys:user:password')")
    public HttpResult<Boolean> password(@RequestParam("id")Long id,@RequestParam("newPassword")String newPassword) {
        return new HttpResult<Boolean>().ok(sysUserApplicationService.updatePassword(id,newPassword));
    }

    @PutMapping("/pwd")
    @Operation(summary = "系统用户>重置密码",description = "系统用户>重置密码")
    public HttpResult<Boolean> pwd(@RequestParam("id")Long id,@RequestParam("newPassword")String newPassword) {
        return new HttpResult<Boolean>().ok(sysUserApplicationService.updatePassword(id,newPassword));
    }

    @PostMapping("/insert")
    @Operation(summary = "系统用户>新增",description = "系统用户>新增")
    @OperateLog(module = "系统用户",name = "用户新增")
    @PreAuthorize("hasAuthority('sys:user:insert')")
    public HttpResult<Boolean> insert(@RequestBody SysUserDTO dto) {
        return new HttpResult<Boolean>().ok(sysUserApplicationService.insertUser(dto));
    }

    @GetMapping("/detail")
    @Operation(summary = "系统用户>详情",description = "系统用户>详情")
    @DataCache(name = "user", key = "#id")
    public HttpResult<SysUserVO> detail(@RequestParam("id") Long id) {
        return new HttpResult<SysUserVO>().ok(sysUserApplicationService.getUserById(id));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "系统用户>删除",description = "系统用户>删除")
    @OperateLog(module = "系统用户",name = "用户删除")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    @DataCache(name = "user", key = "#id",type = CacheEnum.DEL)
    public HttpResult<Boolean> delete(@RequestParam("id") Long id) {
        return new HttpResult<Boolean>().ok(sysUserApplicationService.deleteUser(id));
    }

    @PostMapping("/query")
    @Operation(summary = "系统用户>查询",description = "系统用户>查询")
    @PreAuthorize("hasAuthority('sys:user:query')")
    public HttpResult<IPage<SysUserVO>> query(@RequestBody SysUserQo qo) {
        return new HttpResult<IPage<SysUserVO>>().ok(sysUserApplicationService.queryUserPage(qo));
    }

}
