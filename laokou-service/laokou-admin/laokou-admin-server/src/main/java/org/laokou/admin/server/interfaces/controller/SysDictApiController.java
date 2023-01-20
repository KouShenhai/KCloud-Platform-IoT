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
import org.laokou.admin.server.application.service.SysDictApplicationService;
import org.laokou.admin.client.dto.SysDictDTO;
import org.laokou.admin.server.infrastructure.annotation.DataCache;
import org.laokou.admin.server.interfaces.qo.SysDictQo;
import org.laokou.admin.client.vo.SysDictVO;
import org.laokou.common.swagger.utils.HttpResult;
import org.laokou.admin.server.infrastructure.annotation.OperateLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
/**
 * 系统字典控制器
 * @author laokou
 */
@RestController
@Tag(name = "Sys Dict API",description = "系统字典API")
@RequestMapping("/sys/dict/api")
@RequiredArgsConstructor
public class SysDictApiController {

    private final SysDictApplicationService sysDictApplicationService;

    @PostMapping(value = "/query")
    @Operation(summary = "系统字典>查询",description = "系统字典>查询")
    @PreAuthorize("hasAuthority('sys:dict:query')")
    public HttpResult<IPage<SysDictVO>> query(@RequestBody SysDictQo qo) {
        return new HttpResult<IPage<SysDictVO>>().ok(sysDictApplicationService.queryDictPage(qo));
    }

    @GetMapping(value = "/detail")
    @Operation(summary = "系统字典>详情",description = "系统字典>详情")
    @DataCache(name = CacheConstant.DICT,key = "#id")
    public HttpResult<SysDictVO> detail(@RequestParam("id") Long id) {
        return new HttpResult<SysDictVO>().ok(sysDictApplicationService.getDictById(id));
    }

    @PostMapping(value = "/insert")
    @Operation(summary = "系统字典>新增",description = "系统字典>新增")
    @OperateLog(module = "系统字典",name = "字典新增")
    @PreAuthorize("hasAuthority('sys:dict:insert')")
    public HttpResult<Boolean> insert(@RequestBody SysDictDTO dto) {
        return new HttpResult<Boolean>().ok(sysDictApplicationService.insertDict(dto));
    }

    @PutMapping(value = "/update")
    @Operation(summary = "系统字典>修改",description = "系统字典>修改")
    @OperateLog(module = "系统字典",name = "字典修改")
    @PreAuthorize("hasAuthority('sys:dict:update')")
    @DataCache(name = CacheConstant.DICT,key = "#dto.id",type = CacheEnum.DEL)
    public HttpResult<Boolean> update(@RequestBody SysDictDTO dto) {
        return new HttpResult<Boolean>().ok(sysDictApplicationService.updateDict(dto));
    }

    @DeleteMapping(value = "/delete")
    @Operation(summary = "系统字典>删除",description = "系统字典>删除")
    @OperateLog(module = "系统字典",name = "字典删除")
    @PreAuthorize("hasAuthority('sys:dict:delete')")
    @DataCache(name = CacheConstant.DICT,key = "#id",type = CacheEnum.DEL)
    public HttpResult<Boolean> delete(@RequestParam("id") Long id) {
        return new HttpResult<Boolean>().ok(sysDictApplicationService.deleteDict(id));
    }

}
