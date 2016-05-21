package com.hianzuo.viewinject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: Ryan
 * Date: 14-3-14
 * Time: 上午9:22
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewInject {
    String click() default "";

    int id() default 0;

    String res() default "";
}
