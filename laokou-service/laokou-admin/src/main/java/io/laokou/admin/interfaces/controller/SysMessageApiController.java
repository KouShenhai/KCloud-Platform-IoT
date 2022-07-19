package io.laokou.admin.interfaces.controller;

import io.laokou.admin.interfaces.dto.MessageDTO;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.security.annotation.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Api(value = "系统消息API",protocols = "http",tags = "系统消息API")
@RequestMapping("/sys/message/api")
public class SysMessageApiController {

    @PostMapping("/push")
    @ApiOperation("系统消息>推送")
    @PreAuthorize("sys:message:push")
    public HttpResultUtil<Boolean> push(@RequestBody MessageDTO dto) {
        return new HttpResultUtil<Boolean>().ok(true);
    }

}
