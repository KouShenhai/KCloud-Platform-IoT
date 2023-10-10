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

package org.laokou.admin.common.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ContentType;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.mybatisplus.database.BatchMapper;
import org.laokou.common.mybatisplus.database.dataobject.BaseDO;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author laokou
 */
@Slf4j
public class ExcelUtil {

	private static final int DEFAULT_SIZE = 1000;

	private static final String EXCEL_SUFFIX = ".xlsx";

	private static final String CONTENT_TYPE_VALUE = "application/vnd.ms-excel";

	private static final String CONTENT_DISPOSITION = "Content-disposition";

	private static final String CONTENT_DISPOSITION_VALUE = "attachment;filename=";

	private static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

	public static <T extends BaseDO> void export(List<String> tables, HttpServletResponse response, T param,
			PageQuery pageQuery, BatchMapper<T> batchMapper, Class<?> clazz) {
		export(tables, DEFAULT_SIZE, response, param, pageQuery, batchMapper, clazz);
	}

	@SneakyThrows
	public static <T extends BaseDO> void export(List<String> tables, int size, HttpServletResponse response, T param,
			PageQuery pageQuery, BatchMapper<T> batchMapper, Class<?> clazz) {
		try (ServletOutputStream out = response.getOutputStream();
				ExcelWriter excelWriter = EasyExcel.write(out, clazz).build()) {
			// https://easyexcel.opensource.alibaba.com/docs/current/quickstart/write#%E4%BB%A3%E7%A0%81
			// 设置请求头
			header(response);
			List<T> list = Collections.synchronizedList(new ArrayList<>(size));
			batchMapper.resultListFilter(tables, param, resultContext -> {
				list.add(resultContext.getResultObject());
				if (list.size() % size == 0) {
					writeSheet(list, clazz, excelWriter);
				}
			}, pageQuery);
			if (list.size() % size != 0) {
				writeSheet(list, clazz, excelWriter);
			}
			// 刷新数据
			excelWriter.finish();
		}
		catch (Exception e) {
			log.error("错误信息：{}", e.getMessage());
			fail(response);
		}
	}

	@SneakyThrows
	private static void fail(HttpServletResponse response) {
		response.reset();
		response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.getWriter().println(JacksonUtil.toJsonStr(Result.fail("导出失败")));
	}

	private static void header(HttpServletResponse response) {
		String fileName = DateUtil.format(DateUtil.now(), DateUtil.YYYYMMDDHHMMSS) + EXCEL_SUFFIX;
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(CONTENT_TYPE_VALUE);
		response.setHeader(CONTENT_DISPOSITION,
				CONTENT_DISPOSITION_VALUE + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + EXCEL_SUFFIX);
		response.addHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
	}

	private static <DO> void writeSheet(List<DO> list, Class<?> clazz, ExcelWriter excelWriter) {
		WriteSheet writeSheet = EasyExcel.writerSheet().head(clazz).build();
		// 写数据
		excelWriter.write(ConvertUtil.sourceToTarget(list, clazz), writeSheet);
		list.clear();
	}

}
