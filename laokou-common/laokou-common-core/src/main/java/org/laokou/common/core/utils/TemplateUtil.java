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

package org.laokou.common.core.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import java.io.IOException;
import java.util.Map;

/**
 * @author laokou
 */
public class TemplateUtil extends FreeMarkerTemplateUtils {

	private static final Configuration CONFIGURATION;

	static {
		CONFIGURATION = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
	}

	public static String getContent(String template, Map<String, Object> params) throws IOException, TemplateException {
		Template temp = getTemplate(template);
		return FreeMarkerTemplateUtils.processTemplateIntoString(temp, params);
	}

	private static Template getTemplate(String template) throws IOException {
		return new Template("template", template, CONFIGURATION);
	}

}
