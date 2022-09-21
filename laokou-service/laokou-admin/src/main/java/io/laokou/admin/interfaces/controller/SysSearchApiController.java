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
package io.laokou.admin.interfaces.controller;
import io.laokou.admin.application.service.SysSearchApplicationService;
import io.laokou.admin.infrastructure.common.feign.elasticsearch.form.SearchForm;
import io.laokou.admin.infrastructure.common.feign.elasticsearch.form.SearchVO;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.security.annotation.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
/**
 * 搜索管理控制器
 * @author Kou Shenhai
 */
@RestController
@Api(value = "搜索管理API",protocols = "http",tags = "搜索管理API")
@RequestMapping("/sys/search/api")
public class SysSearchApiController {

    @Autowired
    private SysSearchApplicationService sysSearchApplicationService;

    @PostMapping("/resource")
    @ApiOperation("搜索管理>资源")
    @PreAuthorize("sys:search:resource:query")
    public HttpResultUtil<SearchVO<Map<String,Object>>> searchResource(@RequestBody SearchForm form) {
        return new HttpResultUtil<SearchVO<Map<String,Object>>>().ok(sysSearchApplicationService.searchResource(form));
    }

}
