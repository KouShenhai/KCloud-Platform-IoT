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

package org.laokou.generator.template.model;

import lombok.Data;

/**
 *
 * 代码生成器模板领域对象【实体】.
 *
 * @author laokou
 */
@Data
public class TemplateE {

	/**
	 * ID.
	 */
	private Long id;

	/**
	 * 保存命令.
	 */
	private String saveCmd;

	/**
	 * 修改命令.
	 */
	private String modifyCmd;

	/**
	 * 删除命令.
	 */
	private String removeCmd;

	/**
	 * 分页查询.
	 */
	private String pageQry;

	/**
	 * 查看.
	 */
	private String getQry;

	/**
	 * 导入命令.
	 */
	private String importCmd;

	/**
	 * 导出命令.
	 */
	private String exportCmd;

	/**
	 * 转换器.
	 */
	private String convertor;

	/**
	 * 保存命令执行器.
	 */
	private String saveCmdExe;

	/**
	 * 修改命令执行器.
	 */
	private String modifyCmdExe;

	/**
	 * 删除命令执行器.
	 */
	private String removeCmdExe;

	/**
	 * 分页查询执行器.
	 */
	private String pageQryExe;

	/**
	 * 查看执行器.
	 */
	private String getQryExe;

	/**
	 * 导入命令执行器.
	 */
	private String importCmdExe;

	/**
	 * 导出命令执行器.
	 */
	private String exportCmdExe;

	/**
	 * 实体.
	 */
	private String entity;

	/**
	 * 服务.
	 */
	private String serviceI;

	/**
	 * 服务实现.
	 */
	private String serviceImpl;

	/**
	 * 领域服务.
	 */
	private String domainService;

	/**
	 * 数据对象.
	 */
	private String dataObject;

	/**
	 * 网关.
	 */
	private String gateway;

	/**
	 * 网关实现.
	 */
	private String gatewayImpl;

	/**
	 * 控制器.
	 */
	private String controller;

	/**
	 * 数据映射.
	 */
	private String mapper;

	/**
	 * 数据映射XML.
	 */
	private String mapperXml;

	/**
	 * API.
	 */
	private String api;

	/**
	 * 页面.
	 */
	private String view;

	/**
	 * 表单页面.
	 */
	private String formView;

}
