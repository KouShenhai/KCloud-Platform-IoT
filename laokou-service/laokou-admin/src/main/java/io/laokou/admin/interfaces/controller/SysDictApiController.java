package io.laokou.admin.interfaces.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统字典控制器
 * @author Kou Shenhai
 */
@RestController
@Api(value = "系统字典API",protocols = "http",tags = "系统字典API")
@RequestMapping("/sys/dict/api")
public class SysDictApiController {



}
