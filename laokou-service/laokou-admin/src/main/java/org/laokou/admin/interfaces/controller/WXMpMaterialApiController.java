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

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公众号素材管理
 *
 * @author limingze
 * @create: 2022-07-13 11:25
 * @Version 1.0
 */
@RestController
@RequestMapping("/wx/mp/material/api")
@Api(tags="公众号素材API",value = "公众号素材API",protocols = "http")
public class WXMpMaterialApiController {

//    @GetMapping("page")
//    @ApiOperation("分页")
//    public HttpResultUtil page(String appId, String type, int offset, int limit) throws Exception {
//        if (!this.wxService.switchover(appId)) {
//            throw new IllegalArgumentException(String.format("未找到对应appId=[%s]的配置，请核实！", appId));
//        }
//
//        //素材服务
//        WxMpMaterialService materialService = wxService.getMaterialService();
//
//        if (WxConsts.MaterialType.NEWS.equals(type)) {
//            WxMpMaterialNewsBatchGetResult result = materialService.materialNewsBatchGet(offset, limit);
//
//            return new HttpResultUtil<>().ok(new PageData<>(result.getItems(), result.getTotalCount()));
//        } else {
//            WxMpMaterialFileBatchGetResult result = materialService.materialFileBatchGet(type, offset, limit);
//
//            return new HttpResultUtil<>().ok(new PageData<>(result.getItems(), result.getTotalCount()));
//        }
//
//    }
//
//    @GetMapping("get")
//    @ApiOperation("获取永久素材")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "appId", value = "appId", paramType = "query", required = true, dataType="String") ,
//        @ApiImplicitParam(name = "mediaId", value = "素材ID", paramType = "query", required = true, dataType="String")
//    })
//    public HttpResultUtil get(String appId, String mediaId) throws Exception {
//        if (!this.wxService.switchover(appId)) {
//            throw new IllegalArgumentException(String.format("未找到对应appId=[%s]的配置，请核实！", appId));
//        }
//
//
//        //获取永久素材
//        WxMpMaterialNews data = wxService.getMaterialService().materialNewsInfo(mediaId);
//
//        return new HttpResultUtil().ok(data);
//    }
}
