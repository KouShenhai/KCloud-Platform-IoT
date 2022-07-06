package io.laokou.redis.annotation;

import java.lang.annotation.*;

/**
 * @author Kou Shenhai
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock4j {

    /**
     * 键
     * @return
     */
    String key() default "lock";

    /**
     * 过期时间 单位：毫秒
     * <pre>
     *     过期时间一定是要长于业务的执行时间.
     * </pre>
     */
    long expire() default 30000;

    /**
     * 获取锁超时时间 单位：毫秒
     * <pre>
     *     结合业务,建议该时间不宜设置过长,特别在并发高的情况下.
     * </pre>
     */
    long timeout() default 3000;

}
