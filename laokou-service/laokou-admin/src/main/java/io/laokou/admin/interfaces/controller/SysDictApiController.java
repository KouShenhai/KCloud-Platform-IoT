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
import io.laokou.admin.application.service.SysDictApplicationService;
import io.laokou.admin.interfaces.dto.SysDictDTO;
import io.laokou.admin.interfaces.qo.SysDictQO;
import io.laokou.admin.interfaces.vo.SysDictVO;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.log.annotation.OperateLog;
import io.laokou.security.annotation.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
/**
 * 系统字典控制器
 * @author Kou Shenhai
 */
@RestController
@Api(value = "系统字典API",protocols = "http",tags = "系统字典API")
@RequestMapping("/sys/dict/api")
public class SysDictApiController {

    @Autowired
    private SysDictApplicationService sysDictApplicationService;

    @PostMapping(value = "/query")
    @ApiOperation("系统字典>查询")
    @PreAuthorize("sys:dict:query")
    public HttpResultUtil<IPage<SysDictVO>> query(@RequestBody SysDictQO qo) {
        return new HttpResultUtil<IPage<SysDictVO>>().ok(sysDictApplicationService.queryDictPage(qo));
    }

    @GetMapping(value = "/detail")
    @ApiOperation("系统字典>详情")
    public HttpResultUtil<SysDictVO> detail(@RequestParam("id") Long id) {
        return new HttpResultUtil<SysDictVO>().ok(sysDictApplicationService.getDictById(id));
    }

    @PostMapping(value = "/insert")
    @ApiOperation("系统字典>新增")
    @PreAuthorize("sys:dict:insert")
    @OperateLog(module = "系统字典",name = "字典新增")
    public HttpResultUtil<Boolean> insert(@RequestBody SysDictDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysDictApplicationService.insertDict(dto,request));
    }

    @PutMapping(value = "/update")
    @ApiOperation("系统字典>修改")
    @PreAuthorize("sys:dict:update")
    @OperateLog(module = "系统字典",name = "字典修改")
    public HttpResultUtil<Boolean> update(@RequestBody SysDictDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysDictApplicationService.updateDict(dto,request));
    }

    @DeleteMapping(value = "/delete")
    @ApiOperation("系统字典>删除")
    @PreAuthorize("sys:dict:delete")
    @OperateLog(module = "系统字典",name = "字典删除")
    public HttpResultUtil<Boolean> delete(@RequestParam("id") Long id) {
        return new HttpResultUtil<Boolean>().ok(sysDictApplicationService.deleteDict(id));
    }

}
