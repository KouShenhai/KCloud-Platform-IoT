/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.generator.model;

import static org.laokou.common.i18n.common.constant.StringConstant.ROD;
import static org.laokou.generator.model.Constant.*;

public enum Template {

	SAVE_CMD {
		@Override
		public String getTemplatePath(String path) {
			return path + "/client/saveCmd.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_CLIENT + JAVA_PATH + generatorA.getDomainPackagePath()
				+ "/dto";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "SaveCmd.java";
		}
	},

	MODIFY_CMD {
		@Override
		public String getTemplatePath(String path) {
			return path + "/client/modifyCmd.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_CLIENT + JAVA_PATH + generatorA.getDomainPackagePath()
				+ "/dto";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "ModifyCmd.java";
		}
	},

	REMOVE_CMD {
		@Override
		public String getTemplatePath(String path) {
			return path + "/client/removeCmd.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_CLIENT + JAVA_PATH + generatorA.getDomainPackagePath()
				+ "/dto";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "RemoveCmd.java";
		}
	},

	PAGE_QRY {
		@Override
		public String getTemplatePath(String path) {
			return path + "/client/pageQry.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_CLIENT + JAVA_PATH + generatorA.getDomainPackagePath()
				+ "/dto";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "PageQry.java";
		}
	},

	GET_QRY {
		@Override
		public String getTemplatePath(String path) {
			return path + "/client/getQry.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_CLIENT + JAVA_PATH + generatorA.getDomainPackagePath()
				+ "/dto";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "GetQry.java";
		}
	},

	IMPORT_CMD {
		@Override
		public String getTemplatePath(String path) {
			return path + "/client/importCmd.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_CLIENT + JAVA_PATH + generatorA.getDomainPackagePath()
				+ "/dto";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "ImportCmd.java";
		}
	},

	CONVERTOR {
		@Override
		public String getTemplatePath(String path) {
			return path + "/infrastructure/convertor.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_INFRASTRUCTURE + JAVA_PATH
				+ generatorA.getDomainPackagePath() + "/convertor";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "Convertor.java";
		}
	},

	EXPORT_CMD {
		@Override
		public String getTemplatePath(String path) {
			return path + "/client/exportCmd.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_CLIENT + JAVA_PATH + generatorA.getDomainPackagePath()
				+ "/dto";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "ExportCmd.java";
		}
	},

	SAVE_CMD_EXE {
		@Override
		public String getTemplatePath(String path) {
			return path + "/app/saveCmdExe.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_APP + JAVA_PATH + generatorA.getDomainPackagePath()
				+ "/command";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "SaveCmdExe.java";
		}
	},

	MODIFY_CMD_EXE {
		@Override
		public String getTemplatePath(String path) {
			return path + "/app/modifyCmdExe.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_APP + JAVA_PATH + generatorA.getDomainPackagePath()
				+ "/command";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "ModifyCmdExe.java";
		}
	},

	REMOVE_CMD_EXE {
		@Override
		public String getTemplatePath(String path) {
			return path + "/app/removeCmdExe.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_APP + JAVA_PATH + generatorA.getDomainPackagePath()
				+ "/command";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "RemoveCmdExe.java";
		}
	},

	PAGE_QRY_EXE {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return "";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return "";
		}
	},

	GET_QRY_EXE {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return "";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return "";
		}
	},

	IMPORT_CMD_EXE {
		@Override
		public String getTemplatePath(String path) {
			return path + "/app/importCmdExe.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_APP + JAVA_PATH + generatorA.getDomainPackagePath()
				+ "/command";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "ImportCmdExe.java";
		}
	},

	EXPORT_CMD_EXE {
		@Override
		public String getTemplatePath(String path) {
			return path + "/app/exportCmdExe.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_APP + JAVA_PATH + generatorA.getDomainPackagePath()
				+ "/command";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "ExportCmdExe.java";
		}
	},

	ENTITY {
		@Override
		public String getTemplatePath(String path) {
			return path + "/domain/entity.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_DOMAIN + JAVA_PATH + generatorA.getDomainPackagePath()
				+ "/model";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "E.java";
		}
	},

	SERVICE_I {
		@Override
		public String getTemplatePath(String path) {
			return path + "/client/serviceI.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_CLIENT + JAVA_PATH + generatorA.getDomainPackagePath()
				+ "/api";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "sServiceI.java";
		}
	},

	SERVICE_IMPL {
		@Override
		public String getTemplatePath(String path) {
			return path + "/app/serviceImpl.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_APP + JAVA_PATH + generatorA.getDomainPackagePath()
				+ "/service";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "sServiceImpl.java";
		}
	},

	DOMAIN_SERVICE {
		@Override
		public String getTemplatePath(String path) {
			return path + "/domain/domainService.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_DOMAIN + JAVA_PATH + generatorA.getDomainPackagePath()
				+ "/ability";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "DomainService.java";
		}
	},

	DOMAIN_EVENT {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return "";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return "";
		}
	},

	DO {
		@Override
		public String getTemplatePath(String path) {
			return path + "/infrastructure/do.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_INFRASTRUCTURE + JAVA_PATH
				+ generatorA.getDomainPackagePath() + "/gatewayimpl/database/dataobject";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "DO.java";
		}
	},

	CO {
		@Override
		public String getTemplatePath(String path) {
			return path + "/client/co.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_CLIENT + JAVA_PATH + generatorA.getDomainPackagePath()
				+ "/dto/clientobject";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "CO.java";
		}
	},

	GATEWAY {
		@Override
		public String getTemplatePath(String path) {
			return path + "/domain/gateway.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_DOMAIN + JAVA_PATH + generatorA.getDomainPackagePath()
				+ "/gateway";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "Gateway.java";
		}
	},

	GATEWAY_IMPL {
		@Override
		public String getTemplatePath(String path) {
			return path + "/infrastructure/gatewayImpl.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_INFRASTRUCTURE + JAVA_PATH + generatorA.getDomainPackagePath()
				+ "/gatewayimpl";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "GatewayImpl.java";
		}
	},

	CONTROLLER {
		@Override
		public String getTemplatePath(String path) {
			return path + "/adapter/controller.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_ADAPTER + JAVA_PATH + generatorA.getPackagePath() + "/web";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "sController" + generatorA.getVersion().toUpperCase() + ".java";
		}
	},

	MAPPER_XML {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return "";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return "";
		}
	},

	MAPPER {
		@Override
		public String getTemplatePath(String path) {
			return path + "/infrastructure/mapper.ftl";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return generatorA.getModuleName() + ROD + COLA_INFRASTRUCTURE + JAVA_PATH
				+ generatorA.getDomainPackagePath() + "/gatewayimpl/database";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return generatorA.getTableV().className() + "Mapper.java";
		}
	},

	VIEW {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return "";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return "";
		}
	},

	FORM_VIEW {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return "";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return "";
		}
	},

	API {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(GeneratorA generatorA) {
			return "";
		}

		@Override
		public String getFileName(GeneratorA generatorA) {
			return "";
		}
	};

	public abstract String getTemplatePath(String path);

	public abstract String getFileDirectory(GeneratorA generatorA);

	public abstract String getFileName(GeneratorA generatorA);

}
