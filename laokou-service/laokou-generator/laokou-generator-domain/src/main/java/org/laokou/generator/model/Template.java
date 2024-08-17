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
import static org.laokou.common.i18n.common.constant.StringConstant.SLASH;
import static org.laokou.generator.model.Constant.*;

public enum Template {

	SAVE_CMD {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	MODIFY_CMD {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	REMOVE_CMD {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	PAGE_QRY {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	GET_QRY {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	IMPORT_CMD {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	CONVERTOR {
		@Override
		public String getTemplatePath(String path) {
			return path + SLASH + "convertor.ftl";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return moduleName + ROD + COLA_INFRASTRUCTURE + JAVA_PATH + packageName + "/convertor";
		}

		@Override
		public String getFileName(String className, String version) {
			return className + "Converter.java";
		}
	},

	EXPORT_CMD {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	SAVE_CMD_EXE {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	MODIFY_CMD_EXE {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	REMOVE_CMD_EXE {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	PAGE_QRY_EXE {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	GET_QRY_EXE {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	IMPORT_CMD_EXE {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	EXPORT_CMD_EXE {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	ENTITY {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	SERVICE_I {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	SERVICE_IMPL {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	DOMAIN_SERVICE {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	DOMAIN_EVENT {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	DO {
		@Override
		public String getTemplatePath(String path) {
			return path + SLASH + "do.ftl";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return moduleName + ROD + COLA_INFRASTRUCTURE + JAVA_PATH + packageName
				+ "/gatewayimpl/database/dataobject";
		}

		@Override
		public String getFileName(String className, String version) {
			return className + "DO.java";
		}
	},

	CO {
		@Override
		public String getTemplatePath(String path) {
			return path + SLASH + "co.ftl";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return moduleName + ROD + COLA_CLIENT + JAVA_PATH + packageName + "/dto/clientobject";
		}

		@Override
		public String getFileName(String className, String version) {
			return className + "CO.java";
		}
	},

	GATEWAY {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	GATEWAY_IMPL {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	CONTROLLER {
		@Override
		public String getTemplatePath(String path) {
			return path + SLASH + "controller.ftl";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return moduleName + ROD + COLA_ADAPTER + JAVA_PATH + packageName + "/web";
		}

		@Override
		public String getFileName(String className, String version) {
			return className + "Controller" + version.toUpperCase() + ".java";
		}
	},

	MAPPER_XML {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	MAPPER {
		@Override
		public String getTemplatePath(String path) {
			return path + SLASH + "mapper.ftl";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return moduleName + ROD + COLA_INFRASTRUCTURE + JAVA_PATH + packageName + "/gatewayimpl/database";
		}

		@Override
		public String getFileName(String className, String version) {
			return className + "Mapper.java";
		}
	},

	VIEW {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	FORM_VIEW {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	},

	API {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className, String version) {
			return "";
		}
	};

	public abstract String getTemplatePath(String path);

	public abstract String getFileDirectory(String packageName, String moduleName);

	public abstract String getFileName(String className, String version);

}
