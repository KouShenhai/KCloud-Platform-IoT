package org.laokou.common.sensitive.annotation;

import java.lang.annotation.*;

/**
 * @author laokou
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sensitive {
}
