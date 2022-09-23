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
package org.laokou.admin.interfaces.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.laokou.admin.application.service.SysMessageApplicationService;
import org.laokou.admin.interfaces.dto.MessageDTO;
import org.laokou.admin.interfaces.qo.SysMessageQO;
import org.laokou.admin.interfaces.vo.MessageDetailVO;
import org.laokou.admin.interfaces.vo.SysMessageVO;
import org.laokou.common.utils.HttpResultUtil;
import org.laokou.log.annotation.OperateLog;
import org.laokou.security.annotation.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
/**
 * @author Kou Shenhai
 */
@RestController
@AllArgsConstructor
@Api(value = "系统消息API",protocols = "http",tags = "系统消息API")
@RequestMapping("/sys/message/api")
public class SysMessageApiController {

    @Autowired
    private SysMessageApplicationService sysMessageApplicationService;

    @PostMapping("/insert")
    @ApiOperation("系统消息>新增")
    @PreAuthorize("sys:message:insert")
    @OperateLog(module = "系统消息",name = "消息新增")
    public HttpResultUtil<Boolean> insert(@RequestBody MessageDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysMessageApplicationService.sendMessage(dto,request));
    }

    @PostMapping("/query")
    @ApiOperation("系统消息>查询")
    @PreAuthorize("sys:message:query")
    public HttpResultUtil<IPage<SysMessageVO>> query(@RequestBody SysMessageQO qo) {
        return new HttpResultUtil<IPage<SysMessageVO>>().ok(sysMessageApplicationService.queryMessagePage(qo));
    }

    @GetMapping("/get")
    @ApiOperation("系统消息>查看")
    @OperateLog(module = "系统消息",name = "消息查看")
    public HttpResultUtil<MessageDetailVO> get(@RequestParam("id")Long id) {
        return new HttpResultUtil<MessageDetailVO>().ok(sysMessageApplicationService.getMessageByDetailId(id));
    }

    @GetMapping("/detail")
    @ApiOperation("系统消息>详情")
    @PreAuthorize("sys:message:detail")
    public HttpResultUtil<MessageDetailVO> detail(@RequestParam("id")Long id) {
        return new HttpResultUtil<MessageDetailVO>().ok(sysMessageApplicationService.getMessageById(id));
    }

    @PostMapping("/unread/list")
    @ApiOperation("系统消息>未读")
    public HttpResultUtil<IPage<SysMessageVO>> unread(@RequestBody SysMessageQO qo, HttpServletRequest request) {
        return new HttpResultUtil<IPage<SysMessageVO>>().ok(sysMessageApplicationService.getUnReadList(request,qo));
    }

    @GetMapping("/count")
    @ApiOperation("系统消息>统计")
    public HttpResultUtil<Integer> count(HttpServletRequest request) {
        return new HttpResultUtil<Integer>().ok(sysMessageApplicationService.unReadCount(request));
    }

}
