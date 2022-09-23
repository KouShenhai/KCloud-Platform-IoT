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
import org.springframework.web.bind.annotation.*;
/**
* 公众号自定义菜单
* @author limingze
* @create: 2022-07-13 09:45
* @since 1.0.0
*/
@RestController
@RequestMapping("wx/mp/menu/api/{appId}")
@Api(tags="公众号自定义菜单API",value = "公众号自定义菜单API",protocols = "http")
public class WXMpMenuApiController {
//    private final WXMpMenuService WXMpMenuService;
//    private final WxMpService wxService;
//
//    @GetMapping
//    @ApiOperation("信息")
//    public HttpResultUtil<WXMpMenuDTO> get(@PathVariable("appId") String appId){
//        WXMpMenuDTO data = WXMpMenuService.getByAppId(appId);
//        return new HttpResultUtil<WXMpMenuDTO>().ok(data);
//    }
//
//    @PostMapping
//    @ApiOperation("发布到微信")
//    public HttpResultUtil push(@PathVariable("appId") String appId, @RequestBody WXMpMenuDTO dto) {
//        WXMpMenuDTO data = WXMpMenuService.getByAppId(appId);
//        if (data == null){
//            WXMpMenuService.save(dto);
//        }else {
//            dto.setId(data.getId());
//            WXMpMenuService.update(dto);
//        }
//        if (!this.wxService.switchover(appId)) {
//            throw new IllegalArgumentException(String.format("未找到对应appId=[%s]的配置，请核实！", appId));
//        }
//        //发布到微信
//        try {
//            wxService.getMenuService().menuCreate(data.getMenu());
//        } catch (WxErrorException e) {
//            return new HttpResultUtil().error(e.getMessage());
//        }
//        return new HttpResultUtil();
//    }
//
//    @DeleteMapping
//    @ApiOperation("删除微信菜单")
//    public HttpResultUtil delete(@PathVariable("appId") String appId) {
//        if (!this.wxService.switchover(appId)) {
//            throw new IllegalArgumentException(String.format("未找到对应appId=[%s]的配置，请核实！", appId));
//        }
//        //删除微信菜单
//        try {
//            wxService.getMenuService().menuDelete();
//        } catch (WxErrorException e) {
//            return new HttpResultUtil().error(e.getMessage());
//        }
//        //删除系统菜单
//        WXMpMenuService.deleteByAppId(appId);
//        return new HttpResultUtil();
//    }

}