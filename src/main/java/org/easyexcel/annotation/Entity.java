package org.easyexcel.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author  luyao
 * 配置了该注解的类，在生成对象时都会被视为Excel一行数据所生成的对象
 * @since easyexcel 1.0
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity{
    /**
     * 对应在excel表中sheet
     * @return
     */
    int sheet() default 0;
    /**
     * 对应一个excel文件
     * @return
     */
    String path();
}
