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

public enum Template {

	SAVE_CMD {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	MODIFY_CMD {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	REMOVE_CMD {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	PAGE_QRY {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	GET_QRY {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	IMPORT_CMD {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	CONVERTOR {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	EXPORT_CMD {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	SAVE_CMD_EXE {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	MODIFY_CMD_EXE {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	REMOVE_CMD_EXE {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	PAGE_QRY_EXE {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	GET_QRY_EXE {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	IMPORT_CMD_EXE {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	EXPORT_CMD_EXE {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	ENTITY {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	SERVICE_I {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	SERVICE_IMPL {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	DOMAIN_SERVICE {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	DO {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	CO {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	GATEWAY {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	GATEWAY_IMPL {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	CONTROLLER {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	MAPPER_XML {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	MAPPER {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	VIEW {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	FORM_VIEW {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	},

	API {
		@Override
		public String getTemplatePath(String path) {
			return "";
		}

		@Override
		public String getFilePath(String packageName, String moduleName) {
			return "";
		}
	};

	public abstract String getTemplatePath(String path);

	public abstract String getFilePath(String packageName, String moduleName);

}
