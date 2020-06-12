package org.easyexcel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author luyao
 * 配置了Entity注解的类可配置该注解，配置了该注解的属性后，属性值会用来生赋予Excel行数据对象的一个属性
 * @see Entity
 * @since easyexcel 1.0
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FieldName {
    /**
     * 对应在excel表中的列名
     * @return
     */
    String value() default "";

    /**
     * 对应在excel表中的列号
     * @return
     */
    int col() default -1;
}
