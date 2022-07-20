package io.laokou.admin.interfaces.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.application.service.SysMessageApplicationService;
import io.laokou.admin.interfaces.dto.MessageDTO;
import io.laokou.admin.interfaces.qo.MessageQO;
import io.laokou.admin.interfaces.vo.MessageVO;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.security.annotation.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
@RestController
@AllArgsConstructor
@Api(value = "系统消息API",protocols = "http",tags = "系统消息API")
@RequestMapping("/sys/message/api")
public class SysMessageApiController {

    @Autowired
    private SysMessageApplicationService sysMessageApplicationService;

    @PostMapping("/send")
    @ApiOperation("系统消息>发送")
    @PreAuthorize("sys:message:send")
    public HttpResultUtil<Boolean> send(@RequestBody MessageDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysMessageApplicationService.sendMessage(dto,request));
    }

    @PostMapping("/query")
    @ApiOperation("系统消息>查询")
    @PreAuthorize("sys:message:query")
    public HttpResultUtil<IPage<MessageVO>> query(@RequestBody MessageQO qo) {
        return new HttpResultUtil<IPage<MessageVO>>().ok(null);
    }

}
