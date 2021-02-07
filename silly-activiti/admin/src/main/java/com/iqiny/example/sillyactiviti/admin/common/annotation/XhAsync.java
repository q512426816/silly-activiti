package com.iqiny.example.sillyactiviti.admin.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XhAsync {

	String value() default "";
}
