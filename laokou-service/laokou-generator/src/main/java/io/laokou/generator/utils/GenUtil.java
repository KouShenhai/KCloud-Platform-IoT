package io.laokou.generator.utils;
import freemarker.template.Template;
import io.laokou.common.exception.CustomException;
import org.apache.commons.lang.WordUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
/**
 * 代码生成器工具类
 **/
public class GenUtil {

    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 获取模板渲染后的内容
     * @param content   模板内容
     * @param dataModel 数据模型
     */
    public static String getTemplateContent(String content, Map<String, Object> dataModel) {
        if(dataModel.isEmpty()){
            return content;
        }
        StringReader reader = new StringReader(content);
        StringWriter sw = new StringWriter();
        try {
            //渲染模板
            String templateName = dataModel.getOrDefault("templateName", "generator").toString();
            Template template = new Template(templateName, reader, null, "utf-8");
            template.process(dataModel, sw);
        } catch (Exception e) {
            throw new CustomException("渲染模板失败，请检查模板语法", e);
        }
        content = sw.toString();
        IOUtils.closeQuietly(reader);
        IOUtils.closeQuietly(sw);
        return content;
    }
}