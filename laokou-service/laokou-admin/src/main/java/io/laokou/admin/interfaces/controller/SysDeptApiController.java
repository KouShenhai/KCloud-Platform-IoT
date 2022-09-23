/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.laokou.admin.interfaces.controller;
import io.laokou.admin.application.service.SysDeptApplicationService;
import io.laokou.admin.interfaces.dto.SysDeptDTO;
import io.laokou.admin.interfaces.qo.SysDeptQO;
import org.laokou.common.utils.HttpResultUtil;
import org.laokou.common.vo.SysDeptVO;
import io.laokou.log.annotation.OperateLog;
import io.laokou.security.annotation.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
/**
 * 系统部门
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/26 0026 下午 4:28
 */
@RestController
@Api(value = "系统部门API",protocols = "http",tags = "系统部门API")
@RequestMapping("/sys/dept/api")
public class SysDeptApiController {

    @Autowired
    private SysDeptApplicationService sysDeptApplicationService;

    @GetMapping("/list")
    @ApiOperation("系统部门>列表")
    public HttpResultUtil<SysDeptVO> list() {
        return new HttpResultUtil<SysDeptVO>().ok(sysDeptApplicationService.getDeptList());
    }

    @PostMapping("/query")
    @ApiOperation("系统部门>查询")
    @PreAuthorize("sys:dept:query")
    public HttpResultUtil<List<SysDeptVO>> query(@RequestBody SysDeptQO qo) {
        return new HttpResultUtil<List<SysDeptVO>>().ok(sysDeptApplicationService.queryDeptList(qo));
    }

    @PostMapping("/insert")
    @ApiOperation("系统部门>新增")
    @PreAuthorize("sys:dept:insert")
    @OperateLog(module = "系统部门",name = "部门新增")
    public HttpResultUtil<Boolean> insert(@RequestBody SysDeptDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysDeptApplicationService.insertDept(dto,request));
    }

    @PutMapping("/update")
    @ApiOperation("系统部门>修改")
    @PreAuthorize("sys:dept:update")
    @OperateLog(module = "系统部门",name = "部门修改")
    public HttpResultUtil<Boolean> update(@RequestBody SysDeptDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysDeptApplicationService.updateDept(dto,request));
    }

    @GetMapping("/detail")
    @ApiOperation("系统部门>详情")
    public HttpResultUtil<SysDeptVO> detail(@RequestParam("id")Long id) {
        return new HttpResultUtil<SysDeptVO>().ok(sysDeptApplicationService.getDept(id));
    }

    @DeleteMapping("/delete")
    @ApiOperation("系统部门>删除")
    @PreAuthorize("sys:dept:delete")
    @OperateLog(module = "系统部门",name = "部门删除")
    public HttpResultUtil<Boolean> delete(@RequestParam("id")Long id) {
        return new HttpResultUtil<Boolean>().ok(sysDeptApplicationService.deleteDept(id));
    }

    @GetMapping("/get")
    @ApiOperation("系统部门>部门树ids")
    public HttpResultUtil<List<Long>> get(@RequestParam(value = "roleId")Long roleId) {
        return new HttpResultUtil<List<Long>>().ok(sysDeptApplicationService.getDeptIdsByRoleId(roleId));
    }

}
