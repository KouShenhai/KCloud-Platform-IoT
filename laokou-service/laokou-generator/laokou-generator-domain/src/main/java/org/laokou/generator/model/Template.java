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

import static org.laokou.common.i18n.common.constant.StringConstant.SLASH;

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
		public String getFileName(String className) {
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
		public String getFileName(String className) {
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
		public String getFileName(String className) {
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
		public String getFileName(String className) {
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
		public String getFileName(String className) {
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
		public String getFileName(String className) {
			return "";
		}
	},

	CONVERTOR {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className) {
			return "";
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
		public String getFileName(String className) {
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
		public String getFileName(String className) {
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
		public String getFileName(String className) {
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
		public String getFileName(String className) {
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
		public String getFileName(String className) {
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
		public String getFileName(String className) {
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
		public String getFileName(String className) {
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
		public String getFileName(String className) {
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
		public String getFileName(String className) {
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
		public String getFileName(String className) {
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
		public String getFileName(String className) {
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
		public String getFileName(String className) {
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
			return moduleName + "/src/main/java/" + packageName + "/gatewayimpl/database/dataobject";
		}

		@Override
		public String getFileName(String className) {
			return "DO.java";
		}
	},

	CO {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className) {
			return "";
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
		public String getFileName(String className) {
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
		public String getFileName(String className) {
			return "";
		}
	},

	CONTROLLER {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className) {
			return "";
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
		public String getFileName(String className) {
			return "";
		}
	},

	MAPPER {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFileDirectory(String packageName, String moduleName) {
			return "";
		}

		@Override
		public String getFileName(String className) {
			return "";
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
		public String getFileName(String className) {
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
		public String getFileName(String className) {
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
		public String getFileName(String className) {
			return "";
		}
	};

	public abstract String getTemplatePath(String path);

	public abstract String getFileDirectory(String packageName, String moduleName);

	public abstract String getFileName(String className);

}
