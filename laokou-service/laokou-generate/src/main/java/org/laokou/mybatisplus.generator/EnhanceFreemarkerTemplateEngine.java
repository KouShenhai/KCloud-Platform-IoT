/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.mybatisplus.generator;

import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import jakarta.validation.constraints.NotNull;
import org.laokou.common.i18n.utils.StringUtil;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author Liukefu
 */
public final class EnhanceFreemarkerTemplateEngine extends FreemarkerTemplateEngine {

	@Override
	protected void outputCustomFile(@NotNull List<CustomFile> customFiles, @NotNull TableInfo tableInfo,
			@NotNull Map<String, Object> objectMap) {
		String entityName = tableInfo.getEntityName();
		String entityNameTmp = entityName.substring(0, entityName.length() - 2);
		String dtoPath = this.getPathInfo(OutputFile.entity);
		String rootPath = subBefore(dtoPath, File.separator, true);
		customFiles.forEach((customFile) -> {
			String s = customFile.getFileName();
			List<String> split = List.of(s.split("\\."));
			String path = rootPath + File.separator + split.get(0).toLowerCase();
			String fileName = String.format(path + File.separator + entityNameTmp + "%s", customFile.getFileName());
			this.outputFile(new File(fileName), objectMap, customFile.getTemplatePath(), customFile.isFileOverride());
		});
	}

	public static String subBefore(CharSequence string, CharSequence separator, boolean isLastSeparator) {
		if (StringUtil.isNotEmpty(string)) {
			String str = string.toString();
			String sep = separator.toString();
			if (sep.isEmpty()) {
				return "";
			}
			else {
				int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
				if (-1 == pos) {
					return str;
				}
				else {
					return 0 == pos ? "" : str.substring(0, pos);
				}
			}
		}
		else {
			return null == string ? null : string.toString();
		}
	}

}
