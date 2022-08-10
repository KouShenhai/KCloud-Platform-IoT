package io.laokou.oauth2.controller;
import io.laokou.common.user.BaseUserVO;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.oauth2.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2021/4/16 0016 上午 11:19
 */
@RestController
@RequestMapping("/oauth2")
public class ResourceController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 获取用户名
     * @param principal
     * @return
     */
    @GetMapping("/userInfo")
    public HttpResultUtil<BaseUserVO> getUserInfo(Principal principal) {
        return new HttpResultUtil<BaseUserVO>().ok(sysUserService.getUserInfo(principal.getName()));
    }

}
