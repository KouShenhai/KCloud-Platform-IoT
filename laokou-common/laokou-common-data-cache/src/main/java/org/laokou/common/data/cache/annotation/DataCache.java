package org.laokou.common.data.cache.annotation;
import org.laokou.common.data.cache.enums.CacheEnum;

import java.lang.annotation.*;

/**
 * @author laokou
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataCache {

    /**
     * 缓存名称
     */
    String name();

    /**
     * 缓存键
     */
    String key();

    /**
     * 过期时间
     * 单位秒
     * 默认10分钟
     * @return
     */
    long expire() default 600;

    /**
     * 操作类型
     */
    CacheEnum type() default CacheEnum.GET;

}
