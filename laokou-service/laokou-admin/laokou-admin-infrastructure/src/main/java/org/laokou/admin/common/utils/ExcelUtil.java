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

package org.laokou.admin.common.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.common.collect.Lists;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.laokou.common.mybatisplus.mapper.BaseDO;
import org.laokou.common.mybatisplus.mapper.CrudMapper;
import org.laokou.common.mybatisplus.utils.MybatisUtil;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import static org.laokou.common.i18n.common.constant.StringConstant.DROP;
import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;

/**
 * Excel工具类.
 *
 * @author laokou
 */
@Slf4j
public class ExcelUtil {

	private static final String EXCEL_EXT = ".xlsx";

	private static final int DEFAULT_SIZE = 1000;

	public static <M, T> void doImport(InputStream inputStream, HttpServletResponse response, Class<M> clazz,
			BiConsumer<M, T> consumer, MybatisUtil mybatisUtil) {
		EasyExcel.read(inputStream, new DataListener<>(clazz, consumer, response, mybatisUtil)).sheet().doRead();
	}

	public static <DO extends BaseDO> void doExport(HttpServletResponse response, DO param, PageQuery pageQuery,
			CrudMapper<Long, Integer, DO> crudMapper, Class<?> clazz) {
		doExport(DEFAULT_SIZE, response, param, pageQuery, crudMapper, clazz);
	}

	@SneakyThrows
	public static <DO extends BaseDO> void doExport(int size, HttpServletResponse response, DO param,
			PageQuery pageQuery, CrudMapper<Long, Integer, DO> crudMapper, Class<?> clazz) {
		try (ServletOutputStream out = response.getOutputStream();
				ExcelWriter excelWriter = EasyExcel.write(out, clazz).build()) {
			// 设置请求头
			header(response);
			if (crudMapper.selectObjCount(param, pageQuery) > 0) {
				// https://easyexcel.opensource.alibaba.com/docs/current/quickstart/write#%E4%BB%A3%E7%A0%81
				List<DO> list = Collections.synchronizedList(new ArrayList<>(size));
				crudMapper.selectObjList(param, pageQuery, resultContext -> {
					list.add(resultContext.getResultObject());
					if (list.size() % size == 0) {
						writeSheet(list, clazz, excelWriter);
					}
				});
				if (list.size() % size != 0) {
					writeSheet(list, clazz, excelWriter);
				}
			}
			else {
				excelWriter.write(Collections.singletonList(new Error("数据为空，导出失败")),
						EasyExcel.writerSheet().head(Error.class).build());
			}
			// 刷新数据
			excelWriter.finish();
		}
		catch (Exception e) {
			log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
		}
	}

	private static void header(HttpServletResponse response) {
		String fileName = DateUtil.format(DateUtil.now(), DateUtil.YYYYMMDDHHMMSS) + EXCEL_EXT;
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition",
				"attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + EXCEL_EXT);
		response.addHeader("Access-Control-Expose-Headers", "Content-disposition");
	}

	private static <DO> void writeSheet(List<DO> list, Class<?> clazz, ExcelWriter excelWriter) {
		WriteSheet writeSheet = EasyExcel.writerSheet().head(clazz).build();
		// 写数据
		excelWriter.write(ConvertUtil.sourceToTarget(list, clazz), writeSheet);
		list.clear();
	}

	private static class DataListener<M, T> implements ReadListener<T> {

		public static final int BATCH_COUNT = 1000;

		/**
		 * Temporary storage of data.
		 */
		private final List<T> CACHED_DATA_LIST;

		/**
		 * 错误信息.
		 */
		private final List<String> ERRORS;

		/**
		 * Single handle the amount of data.
		 */
		private final int batchCount;

		private final HttpServletResponse response;

		private final MybatisUtil mybatisUtil;

		private final Class<M> clazz;

		private final BiConsumer<M, T> consumer;

		DataListener(Class<M> clazz, BiConsumer<M, T> consumer, HttpServletResponse response, MybatisUtil mybatisUtil) {
			this(clazz, consumer, BATCH_COUNT, response, mybatisUtil);
		}

		DataListener(Class<M> clazz, BiConsumer<M, T> consumer, int batchCount, HttpServletResponse response,
				MybatisUtil mybatisUtil) {
			this.batchCount = batchCount;
			this.clazz = clazz;
			this.response = response;
			this.ERRORS = new ArrayList<>();
			this.CACHED_DATA_LIST = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
			this.mybatisUtil = mybatisUtil;
			this.consumer = consumer;
		}

		@Override
		public void invoke(T data, AnalysisContext context) {
			int currentRowNum = context.readRowHolder().getRowIndex() + 1;
			// 校验数据
			Set<String> set = ValidatorUtil.validateEntity(data);
			if (CollectionUtil.isNotEmpty(set)) {
				ERRORS.add(template(currentRowNum, StringUtil.collectionToDelimitedString(set, DROP)));
			}
			else {
				CACHED_DATA_LIST.add(data);
				if (CACHED_DATA_LIST.size() % batchCount == 0) {
					mybatisUtil.batch(CACHED_DATA_LIST, clazz, consumer);
					CACHED_DATA_LIST.clear();
				}
			}
		}

		@Override
		public void onException(Exception exception, AnalysisContext context) {
			log.error("Excel导入异常，错误信息：{}，详情见日志", LogUtil.record(exception.getMessage()));
		}

		@SneakyThrows
		@Override
		public void doAfterAllAnalysed(AnalysisContext context) {
			log.info("完成数据解析");
			if (CollectionUtil.isNotEmpty(CACHED_DATA_LIST)) {
				mybatisUtil.batch(CACHED_DATA_LIST, clazz, consumer);
			}
			// 写入excel
			try (ServletOutputStream out = response.getOutputStream();
					ExcelWriter excelWriter = EasyExcel.write(out, Error.class).build()) {
				// 设置请求头
				header(response);
				if (CollectionUtil.isEmpty(ERRORS)) {
					excelWriter.write(Collections.singletonList(new Error(EMPTY)),
							EasyExcel.writerSheet().head(Error.class).build());
				}
				else {
					List<List<String>> partition = Lists.partition(ERRORS, BATCH_COUNT);
					partition.forEach(item -> writeSheet(item, Error.class, excelWriter));
				}
				// 刷新数据
				excelWriter.finish();
			}
		}

		private String template(int num, String msg) {
			return String.format("第%s行，%s", num, msg);
		}

	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	private static class Error {

		@ColumnWidth(30)
		@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
				verticalAlignment = VerticalAlignmentEnum.CENTER, wrapped = BooleanEnum.TRUE)
		@ExcelProperty(value = "错误信息", index = 0)
		private String msg;

	}

}
