package com.ai.generator.excel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author: QIK
 * @CreateDate: 2019/4/30 18:26
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Excel {
    /**
     * 排序
     * @return
     */
    int sort();

    /**
     * 标题
     * @return
     */
    String title() default "";

    /**
     * 日期格式化
     * @return
     */
    String format() default "yyyy-MM-dd HH:mm:ss";
}
