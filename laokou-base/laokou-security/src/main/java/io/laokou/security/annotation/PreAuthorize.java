package io.laokou.security.annotation;

import java.lang.annotation.*;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/18 0018 上午 9:34
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface PreAuthorize {

    String value();

}
