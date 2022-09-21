/**
 * Copyright 2020-2022 Kou Shenhai
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
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.application.service.SysOauthApplicationService;
import io.laokou.admin.interfaces.dto.SysOauthDTO;
import io.laokou.admin.interfaces.qo.SysOauthQO;
import io.laokou.admin.interfaces.vo.SysOauthVO;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.log.annotation.OperateLog;
import io.laokou.security.annotation.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
/**
 * @author Kou Shenhai
 */
@RestController
@Api(value = "系统认证API",protocols = "http",tags = "系统认证API")
@RequestMapping("/sys/oauth/api")
public class SysOauthApiController {

    @Autowired
    private SysOauthApplicationService sysOauthApplicationService;

    @PostMapping("/query")
    @ApiOperation("系统认证>查询")
    @PreAuthorize("sys:oauth:query")
    public HttpResultUtil<IPage<SysOauthVO>> query(@RequestBody SysOauthQO qo) {
        return new HttpResultUtil<IPage<SysOauthVO>>().ok(sysOauthApplicationService.queryOauthPage(qo));
    }

    @GetMapping(value = "/detail")
    @ApiOperation("系统认证>详情")
    public HttpResultUtil<SysOauthVO> detail(@RequestParam("id") Long id) {
        return new HttpResultUtil<SysOauthVO>().ok(sysOauthApplicationService.getOauthById(id));
    }

    @PostMapping(value = "/insert")
    @ApiOperation("系统认证>新增")
    @PreAuthorize("sys:oauth:insert")
    @OperateLog(module = "系统授权",name = "认证新增")
    public HttpResultUtil<Boolean> insert(@RequestBody SysOauthDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysOauthApplicationService.insertOauth(dto,request));
    }

    @PutMapping(value = "/update")
    @ApiOperation("系统认证>修改")
    @PreAuthorize("sys:oauth:update")
    @OperateLog(module = "系统授权",name = "认证修改")
    public HttpResultUtil<Boolean> update(@RequestBody SysOauthDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysOauthApplicationService.updateOauth(dto,request));
    }

    @DeleteMapping(value = "/delete")
    @ApiOperation("系统认证>删除")
    @PreAuthorize("sys:oauth:delete")
    @OperateLog(module = "系统授权",name = "认证删除")
    public HttpResultUtil<Boolean> delete(@RequestParam("id") Long id) {
        return new HttpResultUtil<Boolean>().ok(sysOauthApplicationService.deleteOauth(id));
    }

}
