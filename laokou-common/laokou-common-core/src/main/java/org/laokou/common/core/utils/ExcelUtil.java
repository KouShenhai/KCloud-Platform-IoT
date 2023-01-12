package org.laokou.common.core.utils;
import com.alibaba.excel.EasyExcel;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Date;
/**
 * @author laokou
 * @version 1.0
 * @date 2020/12/26 0026 上午 10:32
 */
public class ExcelUtil {

    private static final String EXCEL_SUFFIX = ".xlsx";

    private static final String CONTENT_TYPE_VALUE = "application/vnd.ms-excel";

    private static final String CONTENT_DISPOSITION = "Content-disposition";

    private static final String CONTENT_DISPOSITION_VALUE = "attachment;filename=";

    private static final String CHART_ENCODE = "UTF-8";

    /**
     * 导出
     * @param response
     * @param list
     * @param clazz
     * @param sheetName
     * @throws IOException
     */
    public static void export(HttpServletResponse response, String sheetName, Collection<?> list, Class<?> clazz) throws IOException {
        String fileName = DateUtil.format(new Date(),DateUtil.DATE_TIME) + EXCEL_SUFFIX;
        response.setCharacterEncoding(CHART_ENCODE);
        response.setContentType(CONTENT_TYPE_VALUE);
        response.setHeader(CONTENT_DISPOSITION,
                CONTENT_DISPOSITION_VALUE + URLEncoder.encode(fileName, CHART_ENCODE) + EXCEL_SUFFIX);
        response.addHeader("Access-Control-Expose-Headers", CONTENT_DISPOSITION);
        ServletOutputStream out = response.getOutputStream();
        EasyExcel.write(out,clazz).sheet(sheetName).doWrite(list);
        out.flush();
        out.close();
    }

}
