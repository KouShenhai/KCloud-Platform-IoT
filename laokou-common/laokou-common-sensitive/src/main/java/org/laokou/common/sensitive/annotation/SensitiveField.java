package org.laokou.common.sensitive.annotation;

import org.laokou.common.sensitive.enums.TypeEnum;

import java.lang.annotation.*;

/**
 * @author laokou
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SensitiveField {
    TypeEnum type();
}
