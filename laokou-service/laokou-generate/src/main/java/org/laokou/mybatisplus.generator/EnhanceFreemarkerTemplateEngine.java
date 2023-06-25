package org.laokou.mybatisplus.generator;

import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Map;

public final class EnhanceFreemarkerTemplateEngine extends FreemarkerTemplateEngine {

    @Override
    protected void outputCustomFile(@NotNull List<CustomFile> customFiles, @NotNull TableInfo tableInfo, @NotNull Map<String, Object> objectMap) {
        String entityName = tableInfo.getEntityName();
        String entityNameTmp = entityName.substring(0,entityName.length()-2);
        String dtoPath = this.getPathInfo(OutputFile.entity);
        String rootPath = subBefore(dtoPath, File.separator, true);
        customFiles.forEach((customFile) -> {
            String s = customFile.getFileName();
            List<String> split = List.of(s.split("\\."));
            String path = rootPath +File.separator + split.get(0).toLowerCase();
            String fileName = String.format(path+File.separator+ entityNameTmp + "%s", customFile.getFileName());
            this.outputFile(new File(fileName), objectMap, customFile.getTemplatePath(),customFile.isFileOverride());
        });
    }

    public static String subBefore(CharSequence string, CharSequence separator, boolean isLastSeparator) {
        if (!StringUtils.isEmpty(string) && separator != null) {
            String str = string.toString();
            String sep = separator.toString();
            if (sep.isEmpty()) {
                return "";
            } else {
                int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
                if (-1 == pos) {
                    return str;
                } else {
                    return 0 == pos ? "" : str.substring(0, pos);
                }
            }
        } else {
            return null == string ? null : string.toString();
        }
    }
}
