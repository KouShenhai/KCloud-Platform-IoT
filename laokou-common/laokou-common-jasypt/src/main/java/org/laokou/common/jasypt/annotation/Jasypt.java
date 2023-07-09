package org.laokou.common.jasypt.annotation;

import org.laokou.common.jasypt.enums.AlgoEnum;

import java.lang.annotation.*;

/**
 * @author laokou
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Jasypt {
    AlgoEnum type() default AlgoEnum.AES;
}
