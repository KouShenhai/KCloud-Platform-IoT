/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.generator.template.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.dto.ClientObject;
import java.time.Instant;

/**
 *
 * 代码生成器模板客户端对象.
 *
 * @author laokou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "代码生成器模板客户端对象", description = "代码生成器模板客户端对象")
public class TemplateCO extends ClientObject {

	@Schema(name = "ID", description = "ID")
	private Long id;

	@Schema(name = "保存命令", description = "保存命令")
	private String saveCmd;

	@Schema(name = "修改命令", description = "修改命令")
	private String modifyCmd;

	@Schema(name = "删除命令", description = "删除命令")
	private String removeCmd;

	@Schema(name = "分页查询", description = "分页查询")
	private String pageQry;

	@Schema(name = "查看", description = "查看")
	private String getQry;

	@Schema(name = "导入命令", description = "导入命令")
	private String importCmd;

	@Schema(name = "导出命令", description = "导出命令")
	private String exportCmd;

	@Schema(name = "转换器", description = "转换器")
	private String convertor;

	@Schema(name = "保存命令执行器", description = "保存命令执行器")
	private String saveCmdExe;

	@Schema(name = "修改命令执行器", description = "修改命令执行器")
	private String modifyCmdExe;

	@Schema(name = "删除命令执行器", description = "删除命令执行器")
	private String removeCmdExe;

	@Schema(name = "分页查询执行器", description = "分页查询执行器")
	private String pageQryExe;

	@Schema(name = "查看执行器", description = "查看执行器")
	private String getQryExe;

	@Schema(name = "导入命令执行器", description = "导入命令执行器")
	private String importCmdExe;

	@Schema(name = "导出命令执行器", description = "导出命令执行器")
	private String exportCmdExe;

	@Schema(name = "实体", description = "实体")
	private String entity;

	@Schema(name = "服务", description = "服务")
	private String serviceI;

	@Schema(name = "服务实现", description = "服务实现")
	private String serviceImpl;

	@Schema(name = "领域服务", description = "领域服务")
	private String domainService;

	@Schema(name = "数据对象", description = "数据对象")
	private String dataObject;

	@Schema(name = "网关", description = "网关")
	private String gateway;

	@Schema(name = "网关实现", description = "网关实现")
	private String gatewayImpl;

	@Schema(name = "控制器", description = "控制器")
	private String controller;

	@Schema(name = "数据映射", description = "数据映射")
	private String mapper;

	@Schema(name = "数据映射XML", description = "数据映射XML")
	private String mapperXml;

	@Schema(name = "API", description = "API")
	private String api;

	@Schema(name = "页面", description = "页面")
	private String view;

	@Schema(name = "表单页面", description = "表单页面")
	private String formView;

	@Schema(name = "创建时间", description = "创建时间")
	private Instant createTime;

}
