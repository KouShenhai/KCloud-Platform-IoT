package io.laokou.common.annotation;
import java.lang.annotation.*;

/**
 * Elasticsearch注解
 * @author Kou Shenhai
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ElasticsearchFieldInfo {

    /**
     * 默认 keyword
     * @return
     */
    String type() default "keyword";

    /**
     * 0 not_analyzed 1 ik_smart 2.ik_max_word 3.ik-index(自定义分词器)
     * @return
     */
    int participle() default 0;

}
