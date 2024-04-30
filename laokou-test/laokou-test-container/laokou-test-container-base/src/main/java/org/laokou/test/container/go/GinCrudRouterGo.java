/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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
 *
 */

package org.laokou.test.container.go;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.TemplateUtil;

import java.util.Map;

@Slf4j
public class GinCrudRouterGo implements Crud {

	public static void main(String[] args) {
		String inst = "t";
		String upper = "Template";
		String lower = "template";
		String desc = "模板";
		Map<String, Object> params = Map.of("inst", inst, "upper", upper, "lower", lower, "desc", desc);
		Crud crud = new GinCrudRouterGo();
		StringBuilder s = new StringBuilder();
		s.append("\n").append(crud.imp(params));
		s.append("\n").append(crud.type(params));
		s.append("\n").append(crud.router(params));
		log.info("\n{}", s);
	}

	@Override
	public String create(Map<String, Object> params) {
		return null;
	}

	@Override
	public String remove(Map<String, Object> params) {
		return null;
	}

	@Override
	public String modify(Map<String, Object> params) {
		return null;
	}

	@Override
	@SneakyThrows
	public String imp(Map<String, Object> params) {
		String str = """
				import (
					"github.com/gin-gonic/gin"
					v1 "lc-base-frame/api/v1"
					"lc-base-frame/middleware"
				)
				""";
		return TemplateUtil.getContent(str, params);
	}

	@Override
	public String findList(Map<String, Object> params) {
		return null;
	}

	@Override
	public String findById(Map<String, Object> params) {
		return null;
	}

	@Override
	public String findOptionList(Map<String, Object> params) {
		return "";
	}

	@Override
	@SneakyThrows
	public String type(Map<String, Object> params) {
		String str = """
				type ${upper}Router struct{}
				""";
		return TemplateUtil.getContent(str, params);
	}

	@Override
	@SneakyThrows
	public String router(Map<String, Object> params) {
		String str = """
				func (s *${upper}Router) Init${upper}Router(Router *gin.RouterGroup) (R gin.IRoutes) {
					// 前缀
					${lower}Router := Router.Group("${lower}").Use(middleware.OperationRecord())
					atSys${upper}Api := v1.ApiGroupApp.SystemApiGroup.AtSys${upper}Api
					{
						${lower}Router.POST("create", atSys${upper}Api.Create${upper})     // 新增${desc}
						${lower}Router.DELETE("remove", atSys${upper}Api.Remove${upper})   // 删除${desc}
						${lower}Router.PUT("modify", atSys${upper}Api.Modify${upper})      // 修改${desc}
						${lower}Router.POST("list", atSys${upper}Api.Find${upper}List)     // 查询${desc}
						${lower}Router.POST("findById", atSys${upper}Api.Find${upper}ById) // 查询${desc}
						${lower}Router.POST("optionList", atSys${upper}Api.Find${upper}OptionList) // 查询${desc}下拉框
					}
					return ${lower}Router
				}
				""";
		return TemplateUtil.getContent(str, params);
	}

}
