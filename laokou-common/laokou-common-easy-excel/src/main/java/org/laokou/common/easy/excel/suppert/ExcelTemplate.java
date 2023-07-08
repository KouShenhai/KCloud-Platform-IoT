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
package org.laokou.common.easy.excel.suppert;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.laokou.common.easy.excel.service.ResultService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.core.utils.DateUtil;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author laokou
 */
public class ExcelTemplate<Q, T> {

	private static final String EXCEL_SUFFIX = ".xlsx";

	private static final String CONTENT_TYPE_VALUE = "application/vnd.ms-excel";

	private static final String CONTENT_DISPOSITION = "Content-disposition";

	private static final String CONTENT_DISPOSITION_VALUE = "attachment;filename=";

	private static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

	/**
	 * 导出请求头
	 * @param response
	 */
	private void header(HttpServletResponse response) {
		String fileName = DateUtil.format(DateUtil.now(), DateUtil.YYYYMMDDHHMMSS) + EXCEL_SUFFIX;
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(CONTENT_TYPE_VALUE);
		response.setHeader(CONTENT_DISPOSITION,
				CONTENT_DISPOSITION_VALUE + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + EXCEL_SUFFIX);
		response.addHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
	}

	/**
	 * 导出
	 * @param chunkSize
	 * @param response
	 * @param qo
	 * @param resultService
	 * @param clazz
	 */
	@SneakyThrows
	public void export(int chunkSize, HttpServletResponse response, Q qo, ResultService<Q, T> resultService,
			Class<?> clazz) {
		// https://easyexcel.opensource.alibaba.com/docs/current/quickstart/write#%E4%BB%A3%E7%A0%81
		// 设置请求头
		header(response);
		List<T> list = Collections.synchronizedList(new ArrayList<>(chunkSize));
		ServletOutputStream out = response.getOutputStream();
		ExcelWriter excelWriter = EasyExcel.write(out, clazz).build();
		resultService.resultList(qo, resultContext -> {
			T t = resultContext.getResultObject();
			list.add(t);
			if (list.size() % chunkSize == 0) {
				writeSheet(list, clazz, excelWriter);
			}
		});
		if (list.size() % chunkSize != 0) {
			writeSheet(list, clazz, excelWriter);
		}
		// 刷新数据
		excelWriter.finish();
		out.flush();
		out.close();
	}

	private void writeSheet(List<T> list, Class<?> clazz, ExcelWriter excelWriter) {
		WriteSheet writeSheet = EasyExcel.writerSheet().head(clazz).build();
		// 写数据
		excelWriter.write(ConvertUtil.sourceToTarget(list, clazz), writeSheet);
		list.clear();
	}

}
