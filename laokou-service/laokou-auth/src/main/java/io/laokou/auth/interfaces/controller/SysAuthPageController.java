package io.laokou.auth.interfaces.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/9 0009 上午 9:07
 */
@Controller
@RequestMapping("/sys")
public class SysAuthPageController {

    @GetMapping("{uri}.html")
    public String page(@PathVariable("uri")String uri) {
        return uri;
    }

}
