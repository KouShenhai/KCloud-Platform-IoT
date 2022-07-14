package io.laokou.log.annotation;
import io.laokou.common.enums.DataTypeEnum;
import java.lang.annotation.*;
/**
 * 自定义操作日志注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog {

    String module() default "";

    String name() default "";

    /**
     * TEXT文本
     * FILE文件
     * @return
     */
    DataTypeEnum type() default DataTypeEnum.TEXT;
}
