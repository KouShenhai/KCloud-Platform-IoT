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

package org.laokou.generator.template.convertor;

import org.laokou.common.core.util.IdGenerator;
import org.laokou.generator.template.dto.clientobject.TemplateCO;
import org.laokou.generator.template.gatewayimpl.database.dataobject.TemplateDO;
import org.laokou.generator.template.model.TemplateE;

/**
 *
 * 代码生成器模板转换器.
 *
 * @author laokou
 */
public class TemplateConvertor {

	public static TemplateDO toDataObject(TemplateE templateE, boolean isInsert) {
		TemplateDO templateDO = new TemplateDO();
		if (isInsert) {
			templateDO.setId(IdGenerator.defaultSnowflakeId());
		}
		else {
			templateDO.setId(templateE.getId());
		}
		templateDO.setSaveCmd(templateE.getSaveCmd());
		templateDO.setModifyCmd(templateE.getModifyCmd());
		templateDO.setRemoveCmd(templateE.getRemoveCmd());
		templateDO.setPageQry(templateE.getPageQry());
		templateDO.setGetQry(templateE.getGetQry());
		templateDO.setImportCmd(templateE.getImportCmd());
		templateDO.setExportCmd(templateE.getExportCmd());
		templateDO.setConvertor(templateE.getConvertor());
		templateDO.setSaveCmdExe(templateE.getSaveCmdExe());
		templateDO.setModifyCmdExe(templateE.getModifyCmdExe());
		templateDO.setRemoveCmdExe(templateE.getRemoveCmdExe());
		templateDO.setPageQryExe(templateE.getPageQryExe());
		templateDO.setGetQryExe(templateE.getGetQryExe());
		templateDO.setImportCmdExe(templateE.getImportCmdExe());
		templateDO.setExportCmdExe(templateE.getExportCmdExe());
		templateDO.setEntity(templateE.getEntity());
		templateDO.setServiceI(templateE.getServiceI());
		templateDO.setServiceImpl(templateE.getServiceImpl());
		templateDO.setDomainService(templateE.getDomainService());
		templateDO.setDataObject(templateE.getDataObject());
		templateDO.setGateway(templateE.getGateway());
		templateDO.setGatewayImpl(templateE.getGatewayImpl());
		templateDO.setController(templateE.getController());
		templateDO.setMapper(templateE.getMapper());
		templateDO.setMapperXml(templateE.getMapperXml());
		templateDO.setApi(templateE.getApi());
		templateDO.setView(templateE.getView());
		templateDO.setFormView(templateE.getFormView());
		return templateDO;
	}

	public static TemplateCO toClientObject(TemplateDO templateDO) {
		TemplateCO templateCO = new TemplateCO();
		templateCO.setSaveCmd(templateDO.getSaveCmd());
		templateCO.setModifyCmd(templateDO.getModifyCmd());
		templateCO.setRemoveCmd(templateDO.getRemoveCmd());
		templateCO.setPageQry(templateDO.getPageQry());
		templateCO.setGetQry(templateDO.getGetQry());
		templateCO.setImportCmd(templateDO.getImportCmd());
		templateCO.setExportCmd(templateDO.getExportCmd());
		templateCO.setConvertor(templateDO.getConvertor());
		templateCO.setSaveCmdExe(templateDO.getSaveCmdExe());
		templateCO.setModifyCmdExe(templateDO.getModifyCmdExe());
		templateCO.setRemoveCmdExe(templateDO.getRemoveCmdExe());
		templateCO.setPageQryExe(templateDO.getPageQryExe());
		templateCO.setGetQryExe(templateDO.getGetQryExe());
		templateCO.setImportCmdExe(templateDO.getImportCmdExe());
		templateCO.setExportCmdExe(templateDO.getExportCmdExe());
		templateCO.setEntity(templateDO.getEntity());
		templateCO.setServiceI(templateDO.getServiceI());
		templateCO.setServiceImpl(templateDO.getServiceImpl());
		templateCO.setDomainService(templateDO.getDomainService());
		templateCO.setDataObject(templateDO.getDataObject());
		templateCO.setGateway(templateDO.getGateway());
		templateCO.setGatewayImpl(templateDO.getGatewayImpl());
		templateCO.setController(templateDO.getController());
		templateCO.setMapper(templateDO.getMapper());
		templateCO.setMapperXml(templateDO.getMapperXml());
		templateCO.setApi(templateDO.getApi());
		templateCO.setView(templateDO.getView());
		templateCO.setFormView(templateDO.getFormView());
		return templateCO;
	}

	public static TemplateE toEntity(TemplateCO templateCO) {
		TemplateE templateE = new TemplateE();
		templateE.setSaveCmd(templateCO.getSaveCmd());
		templateE.setModifyCmd(templateCO.getModifyCmd());
		templateE.setRemoveCmd(templateCO.getRemoveCmd());
		templateE.setPageQry(templateCO.getPageQry());
		templateE.setGetQry(templateCO.getGetQry());
		templateE.setImportCmd(templateCO.getImportCmd());
		templateE.setExportCmd(templateCO.getExportCmd());
		templateE.setConvertor(templateCO.getConvertor());
		templateE.setSaveCmdExe(templateCO.getSaveCmdExe());
		templateE.setModifyCmdExe(templateCO.getModifyCmdExe());
		templateE.setRemoveCmdExe(templateCO.getRemoveCmdExe());
		templateE.setPageQryExe(templateCO.getPageQryExe());
		templateE.setGetQryExe(templateCO.getGetQryExe());
		templateE.setImportCmdExe(templateCO.getImportCmdExe());
		templateE.setExportCmdExe(templateCO.getExportCmdExe());
		templateE.setEntity(templateCO.getEntity());
		templateE.setServiceI(templateCO.getServiceI());
		templateE.setServiceImpl(templateCO.getServiceImpl());
		templateE.setDomainService(templateCO.getDomainService());
		templateE.setDataObject(templateCO.getDataObject());
		templateE.setGateway(templateCO.getGateway());
		templateE.setGatewayImpl(templateCO.getGatewayImpl());
		templateE.setController(templateCO.getController());
		templateE.setMapper(templateCO.getMapper());
		templateE.setMapperXml(templateCO.getMapperXml());
		templateE.setApi(templateCO.getApi());
		templateE.setView(templateCO.getView());
		templateE.setFormView(templateCO.getFormView());
		return templateE;
	}

}
