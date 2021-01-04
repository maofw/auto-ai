package com.ai.generator.aop;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface ApiController {
    /**
     * 是否登陆
     *
     * @return
     */
    boolean isLogin() default false;

    /**
     * 是否进行参数校验
     *
     * @return
     */
    boolean isParam() default true;

    /**
     * 参数接收实体
     *
     * @return
     */
    Class paramClass() default Class.class;

}
