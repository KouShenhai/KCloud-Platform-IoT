package io.laokou.admin.interfaces.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.application.service.SysMessageApplicationService;
import io.laokou.admin.interfaces.dto.MessageDTO;
import io.laokou.admin.interfaces.qo.MessageQO;
import io.laokou.admin.interfaces.vo.MessageDetailVO;
import io.laokou.admin.interfaces.vo.MessageVO;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.security.annotation.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
        return new HttpResultUtil<IPage<MessageVO>>().ok(sysMessageApplicationService.queryMessagePage(qo));
    }

    @GetMapping("/detail")
    @ApiOperation("系统消息>详情")
    @PreAuthorize("sys:message:detail")
    public HttpResultUtil<MessageDetailVO> detail(@RequestParam("id")Long id) {
        return new HttpResultUtil<MessageDetailVO>().ok(sysMessageApplicationService.getMessageById(id));
    }

    @PutMapping("/read")
    @ApiOperation("系统消息>已读")
    public HttpResultUtil<Boolean> read(@RequestParam("id")Long id) {
        return new HttpResultUtil<Boolean>().ok(sysMessageApplicationService.readMessage(id));
    }

    @PostMapping("/unread/list")
    @ApiOperation("系统消息>未读")
    public HttpResultUtil<IPage<MessageVO>> unread(@RequestBody MessageQO qo,HttpServletRequest request) {
        return new HttpResultUtil<IPage<MessageVO>>().ok(sysMessageApplicationService.getUnReadList(request,qo));
    }

    @GetMapping("/count")
    @ApiOperation("系统消息>统计")
    public HttpResultUtil<Integer> count(HttpServletRequest request) {
        return new HttpResultUtil<Integer>().ok(sysMessageApplicationService.unReadCount(request));
    }

}
