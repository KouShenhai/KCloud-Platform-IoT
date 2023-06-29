package org.laokou.common.secret.annotation;

import java.lang.annotation.*;

/**
 * @author laokou
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiSecret {
}
