/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.common.core.utils;
import com.alibaba.excel.EasyExcel;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
/**
 * @author laokou
 */
public class ExcelUtil {

    private static final String EXCEL_SUFFIX = ".xlsx";

    private static final String CONTENT_TYPE_VALUE = "application/vnd.ms-excel";

    private static final String CONTENT_DISPOSITION = "Content-disposition";

    private static final String CONTENT_DISPOSITION_VALUE = "attachment;filename=";

    /**
     * 导出请求头
     * @param response
     * @throws IOException
     */
    public static void exportHeader(HttpServletResponse response) throws IOException {
        String fileName = DateUtil.format(new Date(),DateUtil.DATE_TIME) + EXCEL_SUFFIX;
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(CONTENT_TYPE_VALUE);
        response.setHeader(CONTENT_DISPOSITION,
                CONTENT_DISPOSITION_VALUE + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + EXCEL_SUFFIX);
        response.addHeader("Access-Control-Expose-Headers", CONTENT_DISPOSITION);
    }

}
