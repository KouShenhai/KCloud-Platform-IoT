/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.test.container;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.TemplateUtil;

import java.util.Map;

@Slf4j
public class GinCrudApiGo implements Crud {

	public static void main(String[] args) {
		String inst = "out";
		String upper = "OutputClient";
		String lower = "outputClient";
		Map<String, Object> params = Map.of("inst", inst, "upper", upper, "lower", lower);
		Crud crud = new GinCrudApiGo();
		StringBuilder s = new StringBuilder();
		s.append("\n").append(crud.imp(params));
		s.append("\n").append(crud.type(params));
		s.append("\n").append(crud.create(params));
		s.append("\n").append(crud.remove(params));
		s.append("\n").append(crud.modify(params));
		s.append("\n").append(crud.findList(params));
		s.append("\n").append(crud.findById(params));
		log.info("\n{}", s);
	}

	@SneakyThrows
	public String imp(Map<String, Object> params) {
		String str = """
				import (
					"github.com/gin-gonic/gin"
					"github.com/sirupsen/logrus"
					"go.uber.org/zap"
					"lc-base-frame/model/common/request"
					"lc-base-frame/model/common/response"
					"lc-base-frame/model/system"
					"lc-base-frame/utils"
				)
							""";
		return TemplateUtil.getContent(str, params);
	}

	@SneakyThrows
	public String type(Map<String, Object> params) {
		String str = """
				type AtSys${upper}Api struct{}
							""";
		return TemplateUtil.getContent(str, params);
	}

	@Override
	public String router(Map<String, Object> params) {
		return null;
	}

	@SneakyThrows
	public String findList(Map<String, Object> params) {
		String str = """
				func (api *AtSys${upper}Api) Find${upper}List(c *gin.Context) {
					var pageInfo request.PageInfo
					err := c.ShouldBindJSON(&pageInfo)
					if err != nil {
						response.FailWithMessage(err.Error(), c)
						return
					}
					err = utils.Verify(pageInfo, utils.PageInfoVerify)
					if err != nil {
						response.FailWithMessage(err.Error(), c)
						return
					}
					list, total, err := ${lower}Service.Find${upper}List(pageInfo)
					if err != nil {
						logrus.Error("获取失败!", err)
						response.FailWithMessage("获取失败", c)
						return
					}
					response.OkWithDetailed(response.PageResult{
						List:     list,
						Total:    total,
						Page:     pageInfo.Page,
						PageSize: pageInfo.PageSize,
					}, "获取成功", c)
				}
							""";
		return TemplateUtil.getContent(str, params);
	}

	@Override
	@SneakyThrows
	public String findById(Map<String, Object> params) {
		String str = """
				func (api *AtSys${upper}Api) Find${upper}ById(c *gin.Context) {
					var ${inst} request.FindById
					err := c.ShouldBindJSON(&${inst})
					if err != nil {
						response.FailWithMessage(err.Error(), c)
						return
					}
					err = utils.Verify(${inst}, utils.IdVerify)
					if err != nil {
						response.FailWithMessage(err.Error(), c)
						return
					}
					${lower}Detail, err := ${lower}Service.Find${upper}ById(${inst}.ID)
					if err != nil {
						logrus.Error("查询失败!", err)
						response.FailWithMessage("查询失败", c)
						return
					}
					response.OkWithDetailed(${lower}Detail, "查询成功", c)
				}
							""";
		return TemplateUtil.getContent(str, params);
	}

	@SneakyThrows
	public String create(Map<String, Object> params) {
		String str = """
				func (api *AtSys${upper}Api) Create${upper}(c *gin.Context) {
					var ${inst} system.AtSys${upper}
					err := c.ShouldBindJSON(&${inst})
					if err != nil {
						response.FailWithMessage(err.Error(), c)
						return
					}
					err = utils.Verify(${inst}, utils.${upper}Verify)
					if err != nil {
						response.FailWithMessage(err.Error(), c)
						return
					}
					err = ${lower}Service.Create${upper}(${inst})
					if err != nil {
						logrus.Error("添加失败!", zap.Error(err))
						response.FailWithMessage("添加失败", c)
						return
					}
					response.OkWithMessage("添加成功", c)
				}
							""";
		return TemplateUtil.getContent(str, params);
	}

	@SneakyThrows
	public String modify(Map<String, Object> params) {
		String str = """
				func (api *AtSys${upper}Api) Modify${upper}(c *gin.Context) {
					var ${inst} system.AtSys${upper}
					err := c.ShouldBindJSON(&${inst})
					if err != nil {
						response.FailWithMessage(err.Error(), c)
						return
					}
					err = utils.Verify(${inst}, utils.${upper}Verify)
					if err != nil {
						response.FailWithMessage(err.Error(), c)
						return
					}
					err = ${lower}Service.Modify${upper}(${inst})
					if err != nil {
						logrus.Error("更新失败!", zap.Error(err))
						response.FailWithMessage("更新失败", c)
						return
					}
					response.OkWithMessage("更新成功", c)
				}
							""";
		return TemplateUtil.getContent(str, params);
	}

	@SneakyThrows
	public String remove(Map<String, Object> params) {
		String str = """
				func (api *AtSys${upper}Api) Remove${upper}(c *gin.Context) {
					var ${inst} request.GetById
					err := c.ShouldBindJSON(&${inst})
					if err != nil {
						response.FailWithMessage(err.Error(), c)
						return
					}
					err = utils.Verify(${inst}, utils.IdVerify)
					if err != nil {
						response.FailWithMessage(err.Error(), c)
						return
					}
					err = ${lower}Service.Remove${upper}(${inst}.ID)
					if err != nil {
						logrus.Error("删除失败!", zap.Error(err))
						response.FailWithMessage("删除失败", c)
						return
					}
					response.OkWithMessage("删除成功", c)
				}
							""";
		return TemplateUtil.getContent(str, params);
	}

}
